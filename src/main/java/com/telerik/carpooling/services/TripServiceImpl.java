package com.telerik.carpooling.services;

import com.telerik.carpooling.enums.PassengerStatus;
import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.models.Trip;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.TripDtoResponse;
import com.telerik.carpooling.models.dtos.dtos.mapper.DtoMapper;
import com.telerik.carpooling.models.dtos.TripDtoRequest;
import com.telerik.carpooling.repositories.TripRepository;
import com.telerik.carpooling.repositories.UserRepository;
import com.telerik.carpooling.services.services.contracts.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final DtoMapper dtoMapper;

    @Override
    public TripDtoResponse createTrip(TripDtoRequest tripRequestDto, User driver) {

        Trip trip = dtoMapper.dtoToObject(tripRequestDto);
        trip.setDriver(driver);
        trip.setTripStatus(TripStatus.AVAILABLE);


        return dtoMapper.objectToDto(tripRepository.save(trip));
    }

    @Override
    public TripDtoResponse updateTrip(TripDtoResponse tripDtoResponse) {

        return dtoMapper.objectToDto(tripRepository.save(dtoMapper.dtoToObject(tripDtoResponse)));
    }


    public TripDtoResponse addPassenger (TripDtoResponse tripDtoResponse,User passenger){
        Optional<Trip> trip = tripRepository.findById(tripDtoResponse.getId());

        if (trip.isPresent()){
            if(trip.get().getTripStatus().equals(TripStatus.AVAILABLE)) {
                trip.get().getPassengerStatus().put(passenger, PassengerStatus.PENDING);
                return dtoMapper.objectToDto(tripRepository.save(trip.get()));
            }
        }

        return null;
    }

    public TripDtoResponse changePassengerStatus (TripDtoResponse tripDtoResponse, User passenger,PassengerStatus statusPassenger){
        Optional<Trip> trip = tripRepository.findById(tripDtoResponse.getId());

        if (trip.isPresent()){
            if(statusPassenger.equals(PassengerStatus.CANCELED)) {
                if (trip.get().getPassengerStatus().get(passenger).equals(PassengerStatus.PENDING))
                    trip.get().getPassengerStatus().put(passenger, PassengerStatus.CANCELED);
                else if (trip.get().getPassengerStatus().get(passenger).equals(PassengerStatus.ACCEPTED)) {
                    trip.get().getPassengerStatus().put(passenger, PassengerStatus.CANCELED);
                    trip.get().getPassengersAllowedToRate().remove(passenger);
                    if (trip.get().getTripStatus().equals(TripStatus.BOOKED)) {
                        trip.get().setTripStatus(TripStatus.AVAILABLE);
                    }
                }
                return dtoMapper.objectToDto(tripRepository.save(trip.get()));
            }
        }

        return null;
    }

    public TripDtoResponse approvePassenger(TripDtoResponse tripDtoResponse,int passengerID){
        Optional<Trip> trip = tripRepository.findById(tripDtoResponse.getId());
        Optional<User> passenger = userRepository.findById(passengerID);

        if (trip.isPresent() && passenger.isPresent()
                &&trip.get().getTripStatus().equals(TripStatus.AVAILABLE)) {
            trip.get().getPassengerStatus().put(passenger.get(), PassengerStatus.ACCEPTED);
            trip.get().getPassengersAllowedToRate().add(passenger.get());
            trip.get().getPassengersAvailableForRate().add(passenger.get());
            if(trip.get().getDriver().getCar().getTotalSeats()
                    == trip.get().getPassengerStatus().values()
                    .stream()
                    .filter(k->k.equals(PassengerStatus.ACCEPTED))
                    .count()){
                trip.get().setTripStatus(TripStatus.BOOKED);
            }
            return dtoMapper.objectToDto(tripRepository.save(trip.get()));
        }

        return null;
    }

    public TripDtoResponse rejectPassenger(TripDtoResponse tripDtoResponse,int passengerID){
        Optional<Trip> trip = tripRepository.findById(tripDtoResponse.getId());
        Optional<User> passenger = userRepository.findById(passengerID);

        if (trip.isPresent() && passenger.isPresent()) {
            if(trip.get().getTripStatus().equals(TripStatus.AVAILABLE)||
                    trip.get().getTripStatus().equals(TripStatus.BOOKED)) {
                if (passenger.get().getPassengerStatus().equals(PassengerStatus.ACCEPTED)) {
                    trip.get().getPassengerStatus().put(passenger.get(), PassengerStatus.REJECTED);
                    trip.get().getPassengersAvailableForRate().remove(passenger.get());
                    if (trip.get().getTripStatus().equals(TripStatus.BOOKED)) {
                        trip.get().setTripStatus(TripStatus.AVAILABLE);
                    }
                    return dtoMapper.objectToDto(tripRepository.save(trip.get()));
                }
                else if(passenger.get().getPassengerStatus().equals(PassengerStatus.PENDING)){
                    trip.get().getPassengerStatus().put(passenger.get(), PassengerStatus.REJECTED);
                    return dtoMapper.objectToDto(tripRepository.save(trip.get()));
                }
            }
        }

        return null;
    }

    public TripDtoResponse absentPassenger(TripDtoResponse tripDtoResponse,int passengerID){
        Optional<Trip> trip = tripRepository.findById(tripDtoResponse.getId());
        Optional<User> passenger = userRepository.findById(passengerID);

        if (trip.isPresent() && passenger.isPresent()) {
            if(trip.get().getTripStatus().equals(TripStatus.AVAILABLE)||
                    trip.get().getTripStatus().equals(TripStatus.BOOKED)) {
                if (passenger.get().getPassengerStatus().equals(PassengerStatus.ACCEPTED)) {
                    trip.get().getPassengerStatus().put(passenger.get(), PassengerStatus.ABSENT);
                    trip.get().getPassengersAllowedToRate().remove(passenger.get());
                    if (trip.get().getTripStatus().equals(TripStatus.BOOKED)) {
                        trip.get().setTripStatus(TripStatus.AVAILABLE);
                    }
                    return dtoMapper.objectToDto(tripRepository.save(trip.get()));
                }
            }
        }

        return null;
    }

}

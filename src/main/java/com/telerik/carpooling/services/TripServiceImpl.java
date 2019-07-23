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

        if(driver.getCar() != null) {
            Trip trip = dtoMapper.dtoToObject(tripRequestDto);
            trip.setDriver(driver);
            trip.setTripStatus(TripStatus.AVAILABLE);

            return dtoMapper.objectToDto(tripRepository.save(trip));
        }
        return null;
    }

    @Override
    public TripDtoResponse updateTrip(TripDtoResponse tripDtoResponse) {

        return dtoMapper.objectToDto(tripRepository.save(dtoMapper.dtoToObject(tripDtoResponse)));
    }

    @Override
    public TripDtoResponse changeTripStatus(TripDtoResponse tripDtoResponse, User user, TripStatus tripStatus) {
        Optional<Trip> trip = tripRepository.findById(tripDtoResponse.getId());

        if (trip.isPresent() && trip.get().getDriver().equals(user)) {
            if (tripStatus.equals(TripStatus.BOOKED)
                    && trip.get().getTripStatus().equals(TripStatus.AVAILABLE))
                return markTripAsBooked(tripDtoResponse);
            else if (tripStatus.equals(TripStatus.ONGOING)
                    && (trip.get().getTripStatus().equals(TripStatus.AVAILABLE)
                    || trip.get().getTripStatus().equals(TripStatus.BOOKED)))
                return markTripAsOngoing(tripDtoResponse);
            else if (tripStatus.equals(TripStatus.DONE)
                    && trip.get().getTripStatus().equals(TripStatus.ONGOING))
                return markTripAsDone(tripDtoResponse);
            else if (tripStatus.equals(TripStatus.CANCELED)
                    && !trip.get().getTripStatus().equals(TripStatus.DONE))
                return markTripAsCanceled(tripDtoResponse);
        }
        return null;
    }

    @Override
    public TripDtoResponse addPassenger(TripDtoResponse tripDtoResponse, User passenger) {
        Optional<Trip> trip = tripRepository.findById(tripDtoResponse.getId());
        if (trip.isPresent()) {
            if (trip.get().getTripStatus().equals(TripStatus.AVAILABLE)
                    && !trip.get().getDriver().equals(passenger)) {
                trip.get().getPassengerStatus().put(passenger, PassengerStatus.PENDING);
                return dtoMapper.objectToDto(tripRepository.save(trip.get()));
            }
        }

        return null;
    }

    public TripDtoResponse changePassengerStatus(TripDtoResponse tripDtoResponse, User user,int passengerID, PassengerStatus statusPassenger) {
        Optional<Trip> trip = tripRepository.findById(tripDtoResponse.getId());
        Optional<User> passenger = userRepository.findById(passengerID);

        if (trip.isPresent() && passenger.isPresent()) {
            if (trip.get().getPassengerStatus().keySet().stream().anyMatch(k->k.equals(user))) {
                if (statusPassenger.equals(PassengerStatus.CANCELED)) {
                    return cancelPassenger(user, tripDtoResponse);
                }
            }
            if (trip.get().getDriver().equals(user)
                    && trip.get().getPassengerStatus().keySet().stream().anyMatch(k->k.equals(passenger.get()))) {
                if (statusPassenger.equals(PassengerStatus.ACCEPTED) &&
                        trip.get().getTripStatus().equals(TripStatus.AVAILABLE)) {
                    return acceptPassenger(passengerID, tripDtoResponse);
                }
                else if (statusPassenger.equals(PassengerStatus.REJECTED) &&
                                (trip.get().getTripStatus().equals(TripStatus.AVAILABLE) ||
                                trip.get().getTripStatus().equals(TripStatus.BOOKED))) {
                    return rejectPassenger(passengerID, tripDtoResponse);
                }
                else if (statusPassenger.equals(PassengerStatus.ABSENT) &&
                        (trip.get().getTripStatus().equals(TripStatus.AVAILABLE) ||
                                trip.get().getTripStatus().equals(TripStatus.BOOKED)) &&
                                user.getPassengerStatus().equals(PassengerStatus.ACCEPTED)) {
                    return absentPassenger(tripDtoResponse, passengerID);
                }
            }
        }
        return null;
    }

    private TripDtoResponse cancelPassenger(User user, TripDtoResponse tripDtoResponse) {
        Optional<Trip> trip = tripRepository.findById(tripDtoResponse.getId());

        if (trip.isPresent()) {

            if (trip.get().getPassengerStatus().get(user).equals(PassengerStatus.PENDING)) {
                trip.get().getPassengerStatus().put(user, PassengerStatus.CANCELED);
                return dtoMapper.objectToDto(tripRepository.save(trip.get()));
            }
            else if (trip.get().getPassengerStatus().get(user).equals(PassengerStatus.ACCEPTED)) {
                trip.get().getPassengerStatus().put(user, PassengerStatus.CANCELED);
                trip.get().getPassengersAllowedToRate().remove(user);
                trip.get().getPassengersAllowedToGiveFeedback().remove(user);

                if (trip.get().getTripStatus().equals(TripStatus.BOOKED)) {
                    trip.get().setTripStatus(TripStatus.AVAILABLE);
                }

                return dtoMapper.objectToDto(tripRepository.save(trip.get()));
            }
        }
        return null;
    }

    private TripDtoResponse rejectPassenger(int passengerID, TripDtoResponse tripDtoResponse) {
        Optional<Trip> trip = tripRepository.findById(tripDtoResponse.getId());
        Optional<User> passenger = userRepository.findById(passengerID);
        if (trip.isPresent() && passenger.isPresent()) {
            if (trip.get().getPassengerStatus().get(passenger.get()).equals(PassengerStatus.ACCEPTED)) {
                trip.get().getPassengerStatus().put(passenger.get(), PassengerStatus.REJECTED);
                trip.get().getPassengersAvailableForRate().remove(passenger.get());
                trip.get().getPassengersAvailableForFeedback().remove(passenger.get());
                if (trip.get().getTripStatus().equals(TripStatus.BOOKED)) {
                    trip.get().setTripStatus(TripStatus.AVAILABLE);
                }
                return dtoMapper.objectToDto(tripRepository.save(trip.get()));
            } else if (trip.get().getPassengerStatus().get(passenger.get()).equals(PassengerStatus.PENDING)) {
                trip.get().getPassengerStatus().put(passenger.get(), PassengerStatus.REJECTED);
                return dtoMapper.objectToDto(tripRepository.save(trip.get()));
            }
        }
        return null;
    }

    private TripDtoResponse acceptPassenger(int passengerID, TripDtoResponse tripDtoResponse) {
        Optional<Trip> trip = tripRepository.findById(tripDtoResponse.getId());
        Optional<User> passenger = userRepository.findById(passengerID);
        if (trip.isPresent()&&passenger.isPresent()) {
            trip.get().getPassengerStatus().put(passenger.get(), PassengerStatus.ACCEPTED);
            trip.get().getPassengersAllowedToRate().add(passenger.get());
            trip.get().getPassengersAvailableForRate().add(passenger.get());
            trip.get().getPassengersAllowedToGiveFeedback().add(passenger.get());
            trip.get().getPassengersAvailableForFeedback().add(passenger.get());
            if (trip.get().getAvailablePlaces()
                    == trip.get().getPassengerStatus().values()
                    .stream()
                    .filter(k -> k.equals(PassengerStatus.ACCEPTED))
                    .count()) {
                trip.get().setTripStatus(TripStatus.BOOKED);
            }
            return dtoMapper.objectToDto(tripRepository.save(trip.get()));
        }
        return null;
    }

    private TripDtoResponse absentPassenger(TripDtoResponse tripDtoResponse, int passengerID) {
        Optional<Trip> trip = tripRepository.findById(tripDtoResponse.getId());
        Optional<User> passenger = userRepository.findById(passengerID);

        if (trip.isPresent() && passenger.isPresent()) {
            trip.get().getPassengerStatus().put(passenger.get(), PassengerStatus.ABSENT);
            trip.get().getPassengersAllowedToRate().remove(passenger.get());
            trip.get().getPassengersAllowedToGiveFeedback().remove(passenger.get());
            if (trip.get().getTripStatus().equals(TripStatus.BOOKED))
                trip.get().setTripStatus(TripStatus.AVAILABLE);

            return dtoMapper.objectToDto(tripRepository.save(trip.get()));
        }
        return null;
    }

    private TripDtoResponse markTripAsBooked(TripDtoResponse tripDtoResponse) {
        Optional<Trip> trip = tripRepository.findById(tripDtoResponse.getId());

        if (trip.isPresent()) {
            trip.get().setTripStatus(TripStatus.BOOKED);
            trip.get().getPassengerStatus().values()
                    .stream()
                    .filter(k->k.equals(PassengerStatus.PENDING))
                    .forEach(p->p = PassengerStatus.REJECTED);
            return dtoMapper.objectToDto(tripRepository.save(trip.get()));
        }
        return null;
    }

    private TripDtoResponse markTripAsOngoing(TripDtoResponse tripDtoResponse) {
        Optional<Trip> trip = tripRepository.findById(tripDtoResponse.getId());

        if (trip.isPresent()) {
            trip.get().setTripStatus(TripStatus.ONGOING);
            return dtoMapper.objectToDto(tripRepository.save(trip.get()));
        }
        return null;
    }

    private TripDtoResponse markTripAsDone(TripDtoResponse tripDtoResponse) {
        Optional<Trip> trip = tripRepository.findById(tripDtoResponse.getId());

        if (trip.isPresent()) {
            trip.get().setTripStatus(TripStatus.DONE);
            return dtoMapper.objectToDto(tripRepository.save(trip.get()));
        }
        return null;
    }

    private TripDtoResponse markTripAsCanceled(TripDtoResponse tripDtoResponse) {
        Optional<Trip> trip = tripRepository.findById(tripDtoResponse.getId());

        if (trip.isPresent()) {
            trip.get().setTripStatus(TripStatus.CANCELED);
            return dtoMapper.objectToDto(tripRepository.save(trip.get()));
        }
        return null;
    }

}

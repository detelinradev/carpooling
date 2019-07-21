package com.telerik.carpooling.services;

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

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

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

        return dtoMapper.objectToDto(tripRepository.save(trip));
    }

    @Override
    public TripDtoResponse updateTrip(TripDtoResponse tripDtoResponse) {

        return dtoMapper.objectToDto(tripRepository.save(dtoMapper.dtoToObject(tripDtoResponse)));
    }

    @Override
    public TripDtoResponse rateTrip(TripDtoResponse tripDtoResponse,User passenger, String userRole,
                                   int ratedUserID, String ratedUserRole, int rating) {
        Optional<Trip> trip = tripRepository.findById(tripDtoResponse.getId());
        Optional<User> ratedUser = userRepository.findById(ratedUserID);

        if (trip.isPresent() && ratedUser.isPresent()) {
            if (userRole.equals("driver") && ratedUserRole.equals("passenger")) {
                if (trip.get().getPassengersAvailableForRate().contains(ratedUser.get())) {
                    trip.get().getPassengersAvailableForRate().remove(ratedUser.get());
                    int countRatings = ratedUser.get().getCountRatingsAsPassenger();
                    double averageRate = ratedUser.get().getAverageRatingPassenger();
                    averageRate = (averageRate * countRatings + rating) / (countRatings + 1);
                    ratedUser.get().setAverageRatingPassenger(averageRate);
                    ratedUser.get().setCountRatingsAsPassenger(countRatings + 1);
                    userRepository.save(ratedUser.get());
                }

            } else if (userRole.equals("passenger") && ratedUserRole.equals("driver")) {
                if (trip.get().getPassengersAllowedToRate().contains(passenger)) {
                    trip.get().getPassengersAllowedToRate().remove(passenger);
                        int countRatings = ratedUser.get().getCountRatingsAsDriver();
                        double averageRate = ratedUser.get().getAverageRatingDriver();
                        averageRate = (averageRate * countRatings + rating) / (countRatings + 1);
                        ratedUser.get().setAverageRatingDriver(averageRate);
                        ratedUser.get().setCountRatingsAsDriver(countRatings + 1);
                        userRepository.save(ratedUser.get());
                }
            }
            return dtoMapper.objectToDto(tripRepository.save(trip.get()));
        }
        return null;
    }

    public TripDtoResponse addPassenger (TripDtoResponse tripDtoResponse,User passenger){
        Optional<Trip> trip = tripRepository.findById(tripDtoResponse.getId());

        if (trip.isPresent()){
            trip.get().getPendingPassengers().add(passenger);
            return dtoMapper.objectToDto(tripRepository.save(trip.get()));
        }

        return null;
    }

    public TripDtoResponse approvePassenger(TripDtoResponse tripDtoResponse,int passengerID){
        Optional<Trip> trip = tripRepository.findById(tripDtoResponse.getId());
        Optional<User> passenger = userRepository.findById(passengerID);

        if (trip.isPresent() && passenger.isPresent()) {
            trip.get().getPendingPassengers().remove(passenger.get());
            trip.get().getAcceptedPassengers().add(passenger.get());
            trip.get().getPassengersAllowedToRate().add(passenger.get());
            trip.get().getPassengersAvailableForRate().add(passenger.get());
            return dtoMapper.objectToDto(tripRepository.save(trip.get()));
        }

        return null;
    }

}

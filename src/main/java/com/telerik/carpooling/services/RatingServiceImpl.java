package com.telerik.carpooling.services;

import com.telerik.carpooling.models.Rating;
import com.telerik.carpooling.models.Trip;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.repositories.RatingRepository;
import com.telerik.carpooling.repositories.TripRepository;
import com.telerik.carpooling.repositories.UserRepository;
import com.telerik.carpooling.services.services.contracts.RatingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Log4j2
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;

    @Override
    public Rating rateDriver(String tripID, User passenger, Integer rating) {
        long intTripID = parseStringToInt(tripID);
        Optional<Trip> trip = tripRepository.findByModelIdAndIsDeletedIsFalse(intTripID);
        if (trip.isPresent()&&rating>0 && rating <6) {
            User driver = trip.get().getDriver();
            if (trip.get().getPassengersAllowedToRate().contains(passenger)) {
                trip.get().getPassengersAllowedToRate().remove(passenger);
                Rating ratingObject = new Rating(passenger, driver, rating,true);
                return ratingRepository.save(ratingObject);
            }
        }
        return null;
    }

    @Override
    public Rating ratePassenger(String tripID, User driver, String passengerID, Integer rating) {

        long intTripID = parseStringToInt(tripID);
        long intPassengerID = parseStringToInt(passengerID);

        Optional<Trip> trip = tripRepository.findByModelIdAndIsDeletedIsFalse(intTripID);
        Optional<User> passenger = userRepository.findById(intPassengerID);

        if (trip.isPresent() && passenger.isPresent()&&rating>0 && rating <6 ) {
            if (trip.get().getPassengersAvailableForRate().contains(passenger.get())) {
                trip.get().getPassengersAvailableForRate().remove(passenger.get());
                Rating ratingObject = new Rating(driver, passenger.get(), rating,false);
                return ratingRepository.save(ratingObject);
            }
        }
        return null;
    }

    private long parseStringToInt(String tripID) {
        long intTripID = 0;
        try {
            intTripID = Long.parseLong(tripID);
        } catch (NumberFormatException e) {
            log.error("Exception during parsing", e);
        }
        return intTripID;
    }
}


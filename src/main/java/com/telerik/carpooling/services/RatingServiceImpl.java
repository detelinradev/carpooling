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
    public Rating rateUser(String tripID, User user, String ratedUserID, int rating) {

        long longTripID = parseStringToLong(tripID);
        long longRatedUserID = parseStringToLong(ratedUserID);

        Optional<Trip> trip = tripRepository.findByModelIdAndIsDeleted(longTripID);
        Optional<User> ratedUser = userRepository.findById(longRatedUserID);

        if (trip.isPresent() && ratedUser.isPresent()&&rating>0 && rating <6 ) {
            if (trip.get().getPassengerStatus().containsKey(ratedUser.get())
                    || trip.get().getPassengerStatus().containsKey(user)
                    && (trip.get().getDriver().equals(user) || trip.get().getDriver().equals(ratedUser.get()))) {
                boolean isDriver = trip.get().getDriver().equals(ratedUser.get());
                Rating ratingObject = new Rating(user, ratedUser.get(), rating,isDriver);
                ratingObject.setIsDeleted(false);
                return ratingRepository.save(ratingObject);
            }
        }
        return null;
    }

    private long parseStringToLong(String tripID) {
        long longTripID = 0;
        try {
            longTripID = Long.parseLong(tripID);
        } catch (NumberFormatException e) {
            log.error("Exception during parsing", e);
        }
        return longTripID;
    }
}


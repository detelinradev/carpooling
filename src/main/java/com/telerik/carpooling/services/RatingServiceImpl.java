package com.telerik.carpooling.services;

import com.telerik.carpooling.enums.UserStatus;
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
    public Rating rateUser(String tripID, User user, String ratedUserID, Integer rating) {

        long intTripID = parseStringToInt(tripID);
        long intRatedUserID = parseStringToInt(ratedUserID);

        Optional<Trip> trip = tripRepository.findByModelIdAndIsDeleted(intTripID);
        Optional<User> ratedUser = userRepository.findById(intRatedUserID);

        if (trip.isPresent() && ratedUser.isPresent()&&rating>0 && rating <6 ) {
            if (trip.get().getUserStatus().containsKey(ratedUser.get())
                    &&trip.get().getUserStatus().containsKey(user)) {
                boolean isDriver = trip.get().getUserStatus().get(ratedUser.get()).equals(UserStatus.DRIVER);
                Rating ratingObject = new Rating(user, ratedUser.get(), rating,isDriver);
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


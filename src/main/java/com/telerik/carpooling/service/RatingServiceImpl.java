package com.telerik.carpooling.service;

import com.telerik.carpooling.enums.UserStatus;
import com.telerik.carpooling.exception.MyNotFoundException;
import com.telerik.carpooling.model.Rating;
import com.telerik.carpooling.model.Trip;
import com.telerik.carpooling.model.TripUserStatus;
import com.telerik.carpooling.model.User;
import com.telerik.carpooling.repository.RatingRepository;
import com.telerik.carpooling.repository.TripRepository;
import com.telerik.carpooling.repository.TripUserStatusRepository;
import com.telerik.carpooling.repository.UserRepository;
import com.telerik.carpooling.service.service.contract.RatingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Log4j2
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final TripUserStatusRepository tripUserStatusRepository;

    @Override
    public void createRating(Long tripID, String loggedUserUsername, String ratedUserUsername, Integer rating) throws MyNotFoundException {

        Trip trip = getTripById(tripID);
        User user = findUserByUsername(loggedUserUsername);
        User ratedUser = findUserByUsername(ratedUserUsername);
        List<TripUserStatus> tripUserStatusList = tripUserStatusRepository.findCurrentTripUserStatusForAllUsersByTripAndIsDeletedFalse(trip);
        boolean isDriver = tripUserStatusList.stream().filter(j->j.getUser().equals(ratedUser))
                .anyMatch(k->k.getUserStatus().equals(UserStatus.DRIVER));

        if ( rating > 0 && rating < 6) {
            if (tripUserStatusList.stream().anyMatch(k->k.getUser().equals(ratedUser))
                    && tripUserStatusList.stream().anyMatch(k->k.getUser().equals(user))
                    && (tripUserStatusList.stream().filter(j->j.getUser().equals(user))
                    .anyMatch(k->k.getUserStatus().equals(UserStatus.DRIVER))
                    || isDriver)) {

                Rating ratingObject = new Rating(user, ratedUser, rating, isDriver);

                ratingRepository.save(ratingObject);

            }else throw new IllegalArgumentException("You are not authorized to rate this user");
        }else throw new IllegalArgumentException("Rating value should be between 1 and 5");
    }

    @Override
    public void setUserRating(Long tripId, String ratedUserUsername, Integer rating) {

        User ratedUser = findUserByUsername(ratedUserUsername);
        boolean isDriver = tripUserStatusRepository.findFirstByTripModelIdAndUserUsernameAsDriver(tripId,ratedUserUsername).isPresent();

        if(isDriver){
            ratedUser.setRatingAsDriver(ratingRepository.findAverageRatingByUserAsDriver(ratedUser.getModelId())
                    .orElse(0.0));
        }else {
            ratedUser.setRatingAsPassenger(ratingRepository.findAverageRatingByUserAsPassenger(ratedUser.getModelId())
                    .orElse(0.0));
        }


    }


    private Trip getTripById(Long tripID) throws MyNotFoundException {
        return tripRepository.findByModelIdAndIsDeletedFalse(tripID)
                .orElseThrow(() -> new MyNotFoundException("Trip does not exist"));
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsernameAndIsDeletedFalse(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username is not recognized"));
    }
}


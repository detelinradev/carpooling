package com.telerik.carpooling.service;

import com.telerik.carpooling.enums.UserStatus;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Log4j2
@Transactional
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final TripUserStatusRepository tripUserStatusRepository;

    @Override
    public void createRating(final Long tripID, final String loggedUserUsername, final String ratedUserUsername,
                             final Integer rating) {

        Trip trip = getTripById(tripID);
        User loggedUser = findUserByUsername(loggedUserUsername);
        User ratedUser = findUserByUsername(ratedUserUsername);

        List<TripUserStatus> tripUserStatusList =
                tripUserStatusRepository.findCurrentTripUserStatusForAllUsersByTripAndIsDeletedFalse(trip);

        if (doLoggedUserAndRatedUserBothBelongToTripAndOneOfThemIsDriver(loggedUser, ratedUser, tripUserStatusList)) {

            boolean isDriver = tripUserStatusList
                    .stream()
                    .filter(m -> m.getUser().equals(ratedUser))
                    .anyMatch(k -> k.getUserStatus().equals(UserStatus.DRIVER));

            Rating ratingObject = new Rating(loggedUser, ratedUser, rating, isDriver);

            ratingRepository.save(ratingObject);

        } else throw new IllegalArgumentException("You are not authorized to rate this user");
    }

    @Override
    public void setUserRating(final Long tripId, final String ratedUserUsername, final Integer rating) {

        User ratedUser = findUserByUsername(ratedUserUsername);
        getTripById(tripId);

        boolean isDriver = tripUserStatusRepository
                .findFirstByTripModelIdAndUserUsernameAsDriver(tripId, ratedUserUsername).isPresent();

        double newSum;
        int newCount;
        double newRating;

        DecimalFormat df = new DecimalFormat("#.0");

        if (isDriver) {

            newSum = ratedUser.getSumRatingsAsDriver() + rating;
            newCount = ratedUser.getCountRatingsAsDriver() + 1;
            newRating = newSum / newCount;

            df.format(newRating);

            ratedUser.setRatingAsDriver(newRating);
            ratedUser.setCountRatingsAsDriver(newCount);
            ratedUser.setSumRatingsAsDriver(newSum);

        } else {

            newSum = ratedUser.getSumRatingsAsPassenger() + rating;
            newCount = ratedUser.getCountRatingsAsPassenger() + 1;
            newRating = newSum / newCount;

            df.format(newRating);

            ratedUser.setRatingAsPassenger(newRating);
            ratedUser.setCountRatingsAsPassenger(newCount);
            ratedUser.setSumRatingsAsPassenger(newSum);
        }
        userRepository.save(ratedUser);
    }

    private boolean doLoggedUserAndRatedUserBothBelongToTripAndOneOfThemIsDriver
            (final User loggedUser, final User ratedUser, final List<TripUserStatus> tripUserStatuses) {

        Map<User, UserStatus> userUserStatusMap = tripUserStatuses
                .stream()
                .collect(Collectors.toMap(TripUserStatus::getUser, TripUserStatus::getUserStatus));

        return userUserStatusMap.containsKey(ratedUser) && userUserStatusMap.containsKey(loggedUser)
                && (userUserStatusMap.get(ratedUser).equals(UserStatus.DRIVER)
                || userUserStatusMap.get(loggedUser).equals(UserStatus.DRIVER));
    }

    private Trip getTripById(final Long tripID) {
        return tripRepository.findByModelIdAndIsDeletedFalse(tripID)
                .orElseThrow(() -> new IllegalArgumentException("Trip does not exist"));
    }

    private User findUserByUsername(final String username) {
        return userRepository.findByUsernameAndIsDeletedFalse(username)
                .orElseThrow(() -> new IllegalArgumentException("Username is not recognized"));
    }
}


package com.telerik.carpooling.services;

import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.models.Rating;
import com.telerik.carpooling.models.Trip;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.TripDtoResponse;
import com.telerik.carpooling.models.dtos.UserDtoRequest;
import com.telerik.carpooling.models.dtos.UserDtoResponse;
import com.telerik.carpooling.models.dtos.dtos.mapper.DtoMapper;
import com.telerik.carpooling.repositories.TripRepository;
import com.telerik.carpooling.repositories.UserRepository;
import com.telerik.carpooling.services.services.contracts.RatingService;
import com.telerik.carpooling.services.services.contracts.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TripRepository tripRepository;
    private final RatingService ratingService;
    private final DtoMapper dtoMapper;
    private final BCryptPasswordEncoder bCryptEncoder;

    @Override
    public User save(final UserDtoRequest user) {
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setPassword(bCryptEncoder.encode(user.getPassword()));
        newUser.setAge(user.getAge());
        newUser.setEmail(user.getEmail());
        newUser.setRole("USER");
        newUser.setDeleted(false);
        return userRepository.save(newUser);
    }

    @Override
    public User updateCurrentUserPassword(final String password, final User user) {
        if (isPasswordValid(password)) {
            user.setPassword(bCryptEncoder.encode(password));
            return userRepository.save(user);
        } else return null;
    }

    @Override
    public User updateCurrentUserEmail(final String email, final User user) {
        if (isEmailValid(email)) {
            user.setEmail(email);
            return userRepository.save(user);
        } else return null;
    }

    private boolean isEmailValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    private boolean isPasswordValid(String password) {
        String regex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,128}$";
        return password.matches(regex);
    }

    public User rateDriver(String tripID, User passenger, int rating) {

        int intTripID = parseStringToInt(tripID);

        Optional<Trip> trip = tripRepository.findById(intTripID);

        if (trip.isPresent()) {
            User driver = trip.get().getDriver();
            if (trip.get().getPassengersAvailableForRate().contains(passenger)) {
                trip.get().getPassengersAvailableForRate().remove(passenger);
                ratingService.loggingRating(passenger, driver, rating);
                return calculateAverageRatePassenger(rating, driver);
            }
        }
        return null;
    }

    public User ratePassenger(String tripID, User driver,String passengerID, int rating) {

        int intTripID = parseStringToInt(tripID);
        int intPassengerID = parseStringToInt(passengerID);

        Optional<Trip> trip = tripRepository.findById(intTripID);
        Optional<User> passenger = userRepository.findById(intPassengerID);

        if (trip.isPresent() && passenger.isPresent()) {
            if (trip.get().getPassengersAllowedToRate().contains(passenger.get())) {
                trip.get().getPassengersAllowedToRate().remove(passenger.get());
                ratingService.loggingRating(driver, passenger.get(), rating);
                return calculateAverageRateDriver(rating, passenger.get());
            }
        }
        return null;
    }

    private User calculateAverageRateDriver(int rating, User driver) {
        int newCountRatings = driver.getCountRatingsAsDriver() + 1;
        long newSumRatings = driver.getSumRatingsAsDriver() + rating;
        double newAverageRate = newSumRatings / newCountRatings;

        driver.setAverageRatingDriver(newAverageRate);
        driver.setCountRatingsAsDriver(newCountRatings);
        driver.setSumRatingsAsDriver(newSumRatings);

        return userRepository.save(driver);
    }

    private User calculateAverageRatePassenger(int rating, User passenger) {
        int newCountRatings = passenger.getCountRatingsAsPassenger() + 1;
        long newSumRatings = passenger.getSumRatingsAsPassenger() + rating;
        double newAverageRate = newSumRatings / newCountRatings;

        passenger.setAverageRatingPassenger(newAverageRate);
        passenger.setCountRatingsAsPassenger(newCountRatings);
        passenger.setSumRatingsAsPassenger(newSumRatings);

        return userRepository.save(passenger);
    }

    @Override
    public UserDtoResponse leaveFeedback(TripDtoResponse tripDtoResponse, User user, String userRole,
                                         int userToGetFeedbackId, String userToGetFeedbackRole, String feedback) {
        Optional<Trip> trip = tripRepository.findById(tripDtoResponse.getId());
        Optional<User> userToGetFeedback = userRepository.findById(userToGetFeedbackId);

        if (trip.isPresent() && trip.get().getTripStatus().equals(TripStatus.DONE) && userToGetFeedback.isPresent()) {
            if (userRole.equals("driver") && userToGetFeedbackRole.equals("passenger")) {
                if (trip.get().getPassengersAvailableForFeedback().contains(userToGetFeedback.get())) {
                    trip.get().getPassengersAvailableForFeedback().remove(userToGetFeedback.get());
                    userToGetFeedback.get().getFeedbackAsPassenger().add(feedback);
                    return dtoMapper.objectToDto(userRepository.save(userToGetFeedback.get()));
                }

            } else if (userRole.equals("passenger") && userToGetFeedbackRole.equals("driver")) {
                if (trip.get().getPassengersAllowedToGiveFeedback().contains(user)) {
                    trip.get().getPassengersAllowedToGiveFeedback().remove(user);
                    userToGetFeedback.get().getFeedbackAsDriver().add(feedback);
                    return dtoMapper.objectToDto(userRepository.save(userToGetFeedback.get()));
                }
            }
        }
        return null;
    }

    private int parseStringToInt(String tripID) {
        int intTripID = 0;
        try {
            intTripID = Integer.parseInt(tripID);
        } catch (NumberFormatException e) {
            log.error("Exception during parsing", e);
        }
        return intTripID;
    }
}

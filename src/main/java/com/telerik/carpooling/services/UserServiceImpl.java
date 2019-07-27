package com.telerik.carpooling.services;

import com.telerik.carpooling.models.Trip;
import com.telerik.carpooling.models.User;
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

import java.util.Objects;
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
    public UserDtoResponse save(final UserDtoRequest user) {
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setPassword(bCryptEncoder.encode(user.getPassword()));
        newUser.setEmail(user.getEmail());
        newUser.setRole("USER");
        newUser.setDeleted(false);
        newUser.setPhone(user.getPhone());
        return dtoMapper.objectToDto(userRepository.save(newUser));
    }

    @Override
    public User updateUser(final UserDtoResponse userDtoResponse) {
        return userRepository.save(dtoMapper.dtoToObject(userDtoResponse));
    }

    @Override
    public UserDtoResponse getUser(String username) {
        return dtoMapper.objectToDto(userRepository.findFirstByUsername(username));
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

    public User leaveFeedbackDriver(String tripID, User passenger, String feedback) {

        long intTripID = parseStringToInt(tripID);

        Optional<Trip> trip = tripRepository.findById(intTripID);

        if (trip.isPresent()) {
            User driver = trip.get().getDriver();
            if (trip.get().getPassengersAllowedToGiveFeedback().contains(passenger)) {
                trip.get().getPassengersAllowedToGiveFeedback().remove(passenger);
                driver.getFeedbackAsDriver().add(feedback);
                return userRepository.save(driver);
            }
        }
        return null;
    }

    public User leaveFeedbackPassenger(String tripID, User driver, String passengerID, String feedback) {

        long intTripID = parseStringToInt(tripID);
        long intPassengerID = parseStringToInt(passengerID);

        Optional<Trip> trip = tripRepository.findById(intTripID);
        Optional<User> passenger = userRepository.findById(intPassengerID);

        if (trip.isPresent() && passenger.isPresent()) {
            if (trip.get().getPassengersAvailableForFeedback().contains(passenger.get())) {
                trip.get().getPassengersAvailableForFeedback().remove(passenger.get());
                passenger.get().getFeedbackAsPassenger().add(feedback);
                return userRepository.save(passenger.get());
            }
        }
        return null;
    }

//    @Override
//    public Optional<User> findUser(Long userId) {
//        Objects.requireNonNull(userId);
//        return userRepository.findById(userId);
//    }

//    private User calculateAverageRateDriver(int rating, User driver) {
//        int newCountRatings = driver.getCountRatingsAsDriver() + 1;
//        long newSumRatings = driver.getSumRatingsAsDriver() + rating;
//        double newAverageRate = newSumRatings / newCountRatings;
//
//        driver.setRatingAsDriver(newAverageRate);
//        driver.setCountRatingsAsDriver(newCountRatings);
//        driver.setSumRatingsAsDriver(newSumRatings);
//
//        return userRepository.save(driver);
//    }

//    private User calculateAverageRatePassenger(int rating, User passenger) {
//        int newCountRatings = passenger.getCountRatingsAsPassenger() + 1;
//        long newSumRatings = passenger.getSumRatingsAsPassenger() + rating;
//        double newAverageRate = newSumRatings / newCountRatings;
//
//        passenger.setRatingAsPassenger(newAverageRate);
//        passenger.setCountRatingsAsPassenger(newCountRatings);
//        passenger.setSumRatingsAsPassenger(newSumRatings);
//
//        return userRepository.save(passenger);
//    }

    private long parseStringToInt(String tripID) {
        long intTripID = 0;
        try {
            intTripID = Long.parseLong(tripID);
        } catch (NumberFormatException e) {
            log.error("Exception during parsing", e);
        }
        return intTripID;
    }

    private boolean isEmailValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    private boolean isPasswordValid(String password) {
        String regex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,128}$";
        return password.matches(regex);
    }
}

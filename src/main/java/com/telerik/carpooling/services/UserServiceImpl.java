package com.telerik.carpooling.services;

import com.telerik.carpooling.models.Trip;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.TripDtoResponse;
import com.telerik.carpooling.models.dtos.UserDtoRequest;
import com.telerik.carpooling.models.dtos.UserDtoResponse;
import com.telerik.carpooling.models.dtos.dtos.mapper.DtoMapper;
import com.telerik.carpooling.repositories.RatingRepository;
import com.telerik.carpooling.repositories.TripRepository;
import com.telerik.carpooling.repositories.UserRepository;
import com.telerik.carpooling.services.services.contracts.RatingService;
import com.telerik.carpooling.services.services.contracts.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TripRepository tripRepository;
    private final RatingRepository ratingRepository;
    private final DtoMapper dtoMapper;
    private final BCryptPasswordEncoder bCryptEncoder;

    @Override
    public User save(final UserDtoRequest user) {
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setPassword(bCryptEncoder.encode(user.getPassword()));
        newUser.setEmail(user.getEmail());
        newUser.setRole("USER");
        newUser.setIsDeleted(false);
        newUser.setPhone(user.getPhone());
        return userRepository.save(newUser);
    }

    @Override
    public User updateUser(final UserDtoResponse userDtoResponse) {
        return userRepository.save(dtoMapper.dtoToObject(userDtoResponse));
    }

    @Override
    public User getUser(String username) {
         User user = userRepository.findFirstByUsername(username);
        if (ratingRepository.findAverageRatingByUserAsPassenger(user.getModelId()).isPresent())
            user.setRatingAsPassenger(ratingRepository.findAverageRatingByUserAsPassenger(user.getModelId()).get());
        if (ratingRepository.findAverageRatingByUserAsDriver(user.getModelId()).isPresent())
            user.setRatingAsDriver(ratingRepository.findAverageRatingByUserAsDriver(user.getModelId()).get());

        return user;
    }

    @Override
    public List<UserDtoResponse> getUsers() {
        List<UserDtoResponse> users = dtoMapper.userToDtoList(userRepository.findAllByIsDeletedIsFalse());
        for(UserDtoResponse user : users){
            if (ratingRepository.findAverageRatingByUserAsPassenger(user.getModelId()).isPresent())
                user.setRatingAsPassenger(ratingRepository.findAverageRatingByUserAsPassenger(user.getModelId()).get());
            if (ratingRepository.findAverageRatingByUserAsDriver(user.getModelId()).isPresent())
                user.setRatingAsDriver(ratingRepository.findAverageRatingByUserAsDriver(user.getModelId()).get());
        }
        return users;
    }

    @Override
    public List<TripDtoResponse> getUserOwnTrips(String username) {

        return dtoMapper.tripToDtoList(userRepository.findFirstByUsername(username).getMyTrips());
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

        long intTripID = parseStringToLong(tripID);

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

        long intTripID = parseStringToLong(tripID);
        long intPassengerID = parseStringToLong(passengerID);

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

    private long parseStringToLong(String tripID) {
        long longTripID = 0;
        try {
            longTripID = Long.parseLong(tripID);
        } catch (NumberFormatException e) {
            log.error("Exception during parsing", e);
        }
        return longTripID;
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

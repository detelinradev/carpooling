package com.telerik.carpooling.services;

import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.models.Trip;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.TripDtoResponse;
import com.telerik.carpooling.models.dtos.UserDtoRequest;
import com.telerik.carpooling.models.dtos.UserDtoResponse;
import com.telerik.carpooling.models.dtos.dtos.mapper.DtoMapper;
import com.telerik.carpooling.repositories.TripRepository;
import com.telerik.carpooling.repositories.UserRepository;
import com.telerik.carpooling.services.services.contracts.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TripRepository tripRepository;
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
    public User updateCurrentUserPassword(final String password,final User user){
       if(isPasswordValid(password)) {
           user.setPassword(bCryptEncoder.encode(password));
           return userRepository.save(user);
       }else return null;
    }
    @Override
    public User updateCurrentUserEmail(final String email,final User user){
        if(isEmailValid(email)){
        user.setEmail(email);
        return userRepository.save(user);
        }else return null;
    }
    private boolean isEmailValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }
    private boolean isPasswordValid(String password) {
        String regex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,128}$";
        return password.matches(regex);
    }

    public UserDtoResponse rateUser(TripDtoResponse tripDtoResponse, User passenger, String userRole,
                                    int ratedUserID, String ratedUserRole, int rating) {
        Optional<Trip> trip = tripRepository.findById(tripDtoResponse.getId());
        Optional<User> ratedUser = userRepository.findById(ratedUserID);

        if (trip.isPresent() && ratedUser.isPresent()) {
            if (userRole.equals("driver") && ratedUserRole.equals("passenger")) {
                if (trip.get().getPassengersAvailableForRate().contains(ratedUser.get())) {
                    trip.get().getPassengersAvailableForRate().remove(ratedUser.get());
                    return calculateAverageRatePassenger(rating, ratedUserID);
                }

            } else if (userRole.equals("passenger") && ratedUserRole.equals("driver")) {
                if (trip.get().getPassengersAllowedToRate().contains(passenger)) {
                    trip.get().getPassengersAllowedToRate().remove(passenger);
                    return calculateAverageRateDriver(rating, ratedUserID);
                }
            }
        }
        return null;
    }

    private UserDtoResponse calculateAverageRateDriver(int rating, int ratedUserID) {
        Optional<User> ratedUser = userRepository.findById(ratedUserID);
        if(ratedUser.isPresent()) {
            int newCountRatings = ratedUser.get().getCountRatingsAsDriver() + 1;
            long newSumRatings = ratedUser.get().getSumRatingsAsDriver() + rating;
            double newAverageRate = newSumRatings / newCountRatings;

            ratedUser.get().setAverageRatingDriver(newAverageRate);
            ratedUser.get().setCountRatingsAsDriver(newCountRatings);
            ratedUser.get().setSumRatingsAsDriver(newSumRatings);

            return dtoMapper.objectToDto(userRepository.save(ratedUser.get()));
        }
        return null;
    }
    private UserDtoResponse calculateAverageRatePassenger(int rating, int ratedUserID) {
        Optional<User> ratedUser = userRepository.findById(ratedUserID);
        if(ratedUser.isPresent()) {
            int newCountRatings = ratedUser.get().getCountRatingsAsDriver() + 1;
            long newSumRatings = ratedUser.get().getSumRatingsAsDriver() + rating;
            double newAverageRate = newSumRatings / newCountRatings;

            ratedUser.get().setAverageRatingPassenger(newAverageRate);
            ratedUser.get().setCountRatingsAsPassenger(newCountRatings);
            ratedUser.get().setSumRatingsAsPassenger(newSumRatings);

            return dtoMapper.objectToDto(userRepository.save(ratedUser.get()));
        }
        return null;
    }

    @Override
    public UserDtoResponse leaveFeedback(TripDtoResponse tripDtoResponse, User user,String userRole,
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
}

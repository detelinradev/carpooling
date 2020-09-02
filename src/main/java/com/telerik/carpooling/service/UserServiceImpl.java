package com.telerik.carpooling.service;

import com.telerik.carpooling.enums.UserRole;
import com.telerik.carpooling.model.TripUserStatus;
import com.telerik.carpooling.model.Trip;
import com.telerik.carpooling.model.User;
import com.telerik.carpooling.model.dto.TripDtoResponse;
import com.telerik.carpooling.model.dto.UserDtoEdit;
import com.telerik.carpooling.model.dto.UserDtoRequest;
import com.telerik.carpooling.model.dto.UserDtoResponse;
import com.telerik.carpooling.model.dto.dto.mapper.DtoMapper;
import com.telerik.carpooling.repository.TripUserStatusRepository;
import com.telerik.carpooling.repository.RatingRepository;
import com.telerik.carpooling.repository.UserRepository;
import com.telerik.carpooling.service.service.contract.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;
    private final TripUserStatusRepository tripUserStatusRepository;
    private final DtoMapper dtoMapper;
    private final BCryptPasswordEncoder bCryptEncoder;

    @Override
    public UserDtoResponse save(UserDtoRequest userDtoRequest) {
        User user = dtoMapper.dtoToObject(userDtoRequest);
        user.setPassword(bCryptEncoder.encode(userDtoRequest.getPassword()));
        user.setRole(UserRole.USER);
        user.setIsDeleted(false);
        user.setRatingAsDriver(0.0);
        user.setRatingAsPassenger(0.0);
        return dtoMapper.objectToDto(userRepository.save(user));
    }

    @Override
    public UserDtoResponse updateUser(UserDtoEdit userDtoEdit,String loggedUserUsername) {
        User user = dtoMapper.dtoToObject(userDtoEdit);
        String password = user.getPassword();
        User loggedUser = findUserByUsername(loggedUserUsername);
        if (isRole_AdminOrSameUser( user, loggedUser)) {
            user.setRatingAsPassenger(0.0);
            user.setRatingAsDriver(0.0);
            user.setPassword(bCryptEncoder.encode(password));
            return dtoMapper.objectToDto(userRepository.save(user));
        } else throw new IllegalArgumentException("You are not authorized to edit the user");

    }

    @Override
    public UserDtoResponse getUser(String username, String loggedUserUsername) {
        User user = findUserByUsername(username);
        User loggedUser = findUserByUsername(loggedUserUsername);
        if (isRole_AdminOrSameUser( user, loggedUser)) {
            Double ratingAsPassenger = ratingRepository.findAverageRatingByUserAsPassenger(user.getModelId());
            Double ratingAsDriver = ratingRepository.findAverageRatingByUserAsDriver(user.getModelId());
            if (ratingAsDriver != null)
                user.setRatingAsDriver(ratingAsDriver);
            if (ratingAsPassenger != null)
                user.setRatingAsPassenger(ratingAsPassenger);

            return dtoMapper.objectToDto(user);
        } else throw new IllegalArgumentException("You are not authorized to get information for the user");
    }

    @Override
    public List<UserDtoResponse> getUsers(Integer pageNumber, Integer pageSize, String username, String firstName,
                                          String lastName, String email, String phone) {

        List<User> users = userRepository.findUsers(username, firstName, lastName, email, phone,
                (pageNumber != null ? PageRequest.of(pageNumber, pageSize) : null));
        for (User user : users) {
            Double ratingAsPassenger = ratingRepository.findAverageRatingByUserAsPassenger(user.getModelId());
            Double ratingAsDriver = ratingRepository.findAverageRatingByUserAsDriver(user.getModelId());
            if (ratingAsDriver != null)
                user.setRatingAsDriver(ratingAsDriver);
            if (ratingAsPassenger != null)
                user.setRatingAsPassenger(ratingAsPassenger);
        }
        return dtoMapper.userToDtoList(users);
    }

    @Override
    public List<TripDtoResponse> getUserOwnTrips(String loggedUserUsername) {
        List<Trip> tripsNotDeleted = new ArrayList<>();
        User user = findUserByUsername(loggedUserUsername);
        List<TripUserStatus> tripUserStatusList = tripUserStatusRepository.findAllByUserAndIsDeletedFalse(user);
        tripUserStatusList.stream()
                .filter(k -> !k.getTrip().getIsDeleted())
                .distinct()
                .forEach(j -> tripsNotDeleted.add(j.getTrip()));

        return dtoMapper.tripToDtoList(tripsNotDeleted);
    }

    @Override
    public void deleteUser(String username) {

        User user = findUserByUsername(username);
        List<TripUserStatus> tripUserStatusList = tripUserStatusRepository.findAllByUserAndIsDeletedFalse(user);
        tripUserStatusList.forEach(passenger -> passenger.getTrip().setIsDeleted(true));
        tripUserStatusList.forEach(tripUserStatusRepository::save);
        user.setIsDeleted(true);
        userRepository.save(user);
    }

    @Override
    public List<UserDtoResponse> getTopRatedUsers(Boolean isPassenger) {
        List<User> users = userRepository.findUsers(null, null, null,
                null, null, null);
        if (isPassenger) {
            for (User user : users) {
                Double rating;
                rating = ratingRepository.findAverageRatingByUserAsPassenger(user.getModelId());
                if (rating != null)
                    user.setRatingAsPassenger(rating);
            }
            users.sort((a, b) -> b.getRatingAsPassenger().compareTo(a.getRatingAsPassenger()));
        } else {
            for (User user : users) {
                Double rating;
                rating = ratingRepository.findAverageRatingByUserAsDriver(user.getModelId());
                if (rating != null)
                    user.setRatingAsDriver(rating);
            }
            users.sort((a, b) -> b.getRatingAsDriver().compareTo(a.getRatingAsDriver()));
        }

        return dtoMapper.userToDtoList(users.stream()
                .limit(10)
                .collect(Collectors.toList()));
    }

    private boolean isRole_AdminOrSameUser( User user, User loggedUser) {

        return loggedUser.equals(user) || loggedUser.getRole().equals(UserRole.ADMIN);
    }

    private User findUserByUsername(String username) {

        return userRepository.findByUsernameAndIsDeletedFalse(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username is not recognized"));
    }
}

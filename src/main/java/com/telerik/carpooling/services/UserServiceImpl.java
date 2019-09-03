package com.telerik.carpooling.services;

import com.telerik.carpooling.models.TripUserStatus;
import com.telerik.carpooling.models.Trip;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.TripDtoResponse;
import com.telerik.carpooling.models.dtos.UserDtoEdit;
import com.telerik.carpooling.models.dtos.UserDtoRequest;
import com.telerik.carpooling.models.dtos.UserDtoResponse;
import com.telerik.carpooling.models.dtos.dtos.mapper.DtoMapper;
import com.telerik.carpooling.repositories.TripUserStatusRepository;
import com.telerik.carpooling.repositories.RatingRepository;
import com.telerik.carpooling.repositories.UserRepository;
import com.telerik.carpooling.services.services.contracts.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
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
        User newUser = new User();
        User user = dtoMapper.dtoToObject(userDtoRequest);
        newUser.setUsername(user.getUsername());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setPassword(bCryptEncoder.encode(user.getPassword()));
        newUser.setEmail(user.getEmail());
        newUser.setRole("USER");
        newUser.setIsDeleted(false);
        newUser.setPhone(user.getPhone());
        newUser.setRatingAsDriver(0.0);
        newUser.setRatingAsPassenger(0.0);
        return dtoMapper.objectToDto(userRepository.save(newUser));
    }

    @Override
    public UserDtoResponse updateUser(UserDtoEdit userDtoEdit, Authentication authentication) {
        User user = dtoMapper.dtoToObject(userDtoEdit);
        String password = user.getPassword();
        User loggedUser = findUserByUsername(authentication.getName());
        if (isRole_AdminOrSameUser(authentication, user, loggedUser)) {
            user.setRatingAsPassenger(0.0);
            user.setRatingAsDriver(0.0);
            user.setPassword(bCryptEncoder.encode(password));
            return dtoMapper.objectToDto(userRepository.save(user));
        } else throw new IllegalArgumentException("You are not authorized to edit the user");

    }

    @Override
    public UserDtoResponse getUser(String username, Authentication authentication) {
        User user = findUserByUsername(username);
        User loggedUser = findUserByUsername(authentication.getName());
        if (isRole_AdminOrSameUser(authentication, user, loggedUser)) {
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
        for (User user : users) {
            Double rating;
            if (isPassenger)
                rating = ratingRepository.findAverageRatingByUserAsPassenger(user.getModelId());
            else
                rating = ratingRepository.findAverageRatingByUserAsDriver(user.getModelId());
            if (rating != null)
                user.setRatingAsPassenger(rating);
        }
        users.sort((a, b) -> b.getRatingAsPassenger().compareTo(a.getRatingAsPassenger()));

        return dtoMapper.userToDtoList(users.stream()
                .limit(10)
                .collect(Collectors.toList()));
    }

    private boolean isRole_AdminOrSameUser(Authentication authentication, User user, User loggedUser) {

        return loggedUser.equals(user) || authentication.getAuthorities()
                .stream()
                .anyMatch(k -> k.getAuthority().equals("ROLE_ADMIN"));
    }

    private User findUserByUsername(String username) {

        return userRepository.findByUsernameAndIsDeletedFalse(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username is not recognized"));
    }
}

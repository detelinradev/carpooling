package com.telerik.carpooling.services;

import com.telerik.carpooling.models.Trip;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.base.MappedAudibleBase;
import com.telerik.carpooling.models.dtos.TripDtoResponse;
import com.telerik.carpooling.models.dtos.dtos.mapper.DtoMapper;
import com.telerik.carpooling.repositories.RatingRepository;
import com.telerik.carpooling.repositories.TripRepository;
import com.telerik.carpooling.repositories.UserRepository;
import com.telerik.carpooling.services.services.contracts.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.jar.JarOutputStream;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;
    private final DtoMapper dtoMapper;
    private final BCryptPasswordEncoder bCryptEncoder;

    @Override
    public User save(final User user) {
        User newUser = new User();
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
        return userRepository.save(newUser);
    }

    @Override
    public User updateUser(final User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUser(String username) {
        User user = userRepository.findFirstByUsernameAndIsDeletedIsFalse(username);
        Double ratingAsPassenger = ratingRepository.findAverageRatingByUserAsPassenger(user.getModelId());
        Double ratingAsDriver = ratingRepository.findAverageRatingByUserAsDriver(user.getModelId());
        if (ratingAsDriver != null)
            user.setRatingAsDriver(ratingAsDriver);
        if (ratingAsPassenger != null)
            user.setRatingAsPassenger(ratingAsPassenger);

        return user;
    }

    @Override
    public List<User> getUsers(Integer pageNumber, Integer pageSize, String username, String firstName, String lastName, String email,
                               String phone) {

        List<User> users = userRepository.findUsers(username, firstName, lastName, email, phone, (pageNumber != null ? PageRequest.of(pageNumber, pageSize) : null));
        for (User user : users) {
            Double ratingAsPassenger = ratingRepository.findAverageRatingByUserAsPassenger(user.getModelId());
            Double ratingAsDriver = ratingRepository.findAverageRatingByUserAsDriver(user.getModelId());
            if (ratingAsDriver != null)
                user.setRatingAsDriver(ratingAsDriver);
            if (ratingAsPassenger != null)
                user.setRatingAsPassenger(ratingAsPassenger);
        }
        return users;
    }

    @Override
    public List<TripDtoResponse> getUserOwnTrips(String username) {
        List<Trip> tripsNotDeleted = userRepository.findFirstByUsernameAndIsDeletedIsFalse(username).getMyTrips().keySet()
                .stream().filter(trip -> !trip.getIsDeleted())
                .collect(Collectors.toList());

        return dtoMapper.tripToDtoList(tripsNotDeleted);
    }

    @Override
    public User deleteUser(String username) {

        User user = userRepository.findFirstByUsernameAndIsDeletedIsFalse(username);
        user.setIsDeleted(true);
        return userRepository.save(user);
    }

    @Override
    public List<User> getTopRatedPassengers(Integer pageNumber, Integer pageSize, String username,
                                            String firstName, String lastName, String email, String phone) {
        List<User> users = userRepository.findUsers(username, firstName, lastName, email, phone, (pageNumber != null
                ? PageRequest.of(pageNumber, pageSize) : null));
        for (User user : users) {
            Double rating = ratingRepository.findAverageRatingByUserAsPassenger(user.getModelId());
            if (rating != null)
                user.setRatingAsPassenger(rating);
        }
        users.sort((a, b) -> b.getRatingAsPassenger().compareTo(a.getRatingAsPassenger()));

        return users.stream()
                .limit(10)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getTopRatedDrivers(Integer pageNumber, Integer pageSize, String username, String firstName,
                                         String lastName, String email, String phone) {
        List<User> users = userRepository.findUsers(username, firstName, lastName, email, phone, (pageNumber != null
                ? PageRequest.of(pageNumber, pageSize) : null));
        for (User user : users) {
            Double rating = ratingRepository.findAverageRatingByUserAsDriver(user.getModelId());
            if (rating != null)
                user.setRatingAsDriver(rating);
        }
        users.sort((a, b) -> b.getRatingAsDriver().compareTo(a.getRatingAsDriver()));
        return users.stream()
                .limit(10)
                .collect(Collectors.toList());
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

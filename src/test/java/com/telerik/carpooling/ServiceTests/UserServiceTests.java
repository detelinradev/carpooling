package com.telerik.carpooling.ServiceTests;


import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.enums.UserRole;
import com.telerik.carpooling.enums.UserStatus;
import com.telerik.carpooling.model.Car;
import com.telerik.carpooling.model.Trip;
import com.telerik.carpooling.model.TripUserStatus;
import com.telerik.carpooling.model.User;
import com.telerik.carpooling.model.dto.*;
import com.telerik.carpooling.model.dto.dto.mapper.DtoMapper;
import com.telerik.carpooling.repository.TripRepository;
import com.telerik.carpooling.repository.UserRepository;
import com.telerik.carpooling.service.UserServiceImpl;
import com.telerik.carpooling.service.service.contract.TripService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UserServiceTests {

    @Mock
    TripRepository tripRepository;
    @Mock
    TripService tripService;

    @Mock
    UserRepository userRepository;

    @Mock
    BCryptPasswordEncoder bCryptEncoder;

    @Mock
    DtoMapper dtoMapper;

    private Trip trip;
    private TripDtoResponse tripDtoResponse;
    private TripDtoEdit tripDtoEdit;
    private TripDtoRequest tripDtoRequest;
    private TripUserStatus tripUserStatus;
    private User user;
    private UserDtoRequest userDtoRequest;
    private UserDtoResponse userDtoResponse;
    private Car car;
    private List<TripUserStatus> tripUserStatusList;

    @Spy
    @InjectMocks
    UserServiceImpl userService;

    @Before
    public void SetUp() {

        user = new User("username1", "lastName", "username1",
                "email@gmail.com", UserRole.USER, "password", "phone", 3.5,
                4.0);
        user.setModelId(1L);
        userDtoRequest = new UserDtoRequest("username1", "lastName", "username1",
                "email@gmail.com", "password", "phone");
        userDtoResponse = new UserDtoResponse(1L, "username1", "lastName", "username1",
                "email@gmail.com", UserRole.USER, "phone", 3.5, 4.0);
        car = new Car("model", "brand", "color", 2018, true, user);
        trip = new Trip("message", LocalDateTime.MAX,
                "origin", "destination", 3, 5, 4,
                true, true, true, true, TripStatus.AVAILABLE);
        trip.setModelId(1L);
        tripDtoResponse = new TripDtoResponse(1L, "message", LocalDateTime.MAX,
                "origin", "destination", 3, TripStatus.AVAILABLE, 4,
                4, true, true, true, true);
        tripDtoRequest = new TripDtoRequest("message", LocalDateTime.MAX,
                "origin", "destination", 3, 5,
                4, true, true, true, true);
        tripDtoEdit = new TripDtoEdit(1L, "message", LocalDateTime.MAX,
                "origin", "destination", 3, 4,
                4, true, true, true, true);
        tripUserStatus = new TripUserStatus(user, trip, UserStatus.PENDING);
        tripUserStatusList = new ArrayList<>();
        tripUserStatusList.add(tripUserStatus);
    }

    @Test
    public void create_user_Should_CreateNewUser_When_DtoIsValid() {

        when(userRepository.save(user)).thenReturn(user);
        when(dtoMapper.dtoToObject(userDtoRequest)).thenReturn(user);
        when(dtoMapper.objectToDto(user)).thenReturn(userDtoResponse);

        Assert.assertEquals(userDtoResponse, userService.createUser(userDtoRequest));
    }


//    @Test
//    public void findAll_Should_ReturnAll() {
//        //Arrange
//        User first = new User("firstName", "lastName", "username1",
//                "email@gmail.com", UserRole.USER, "password", "phone", 3.5,
//                4.0);
//        User second = new User("firstName", "lastName", "username1",
//                "email@gmail.com", UserRole.USER, "password", "phone", 3.5,
//                4.0);
//        first.setModelId(1L);
//        Optional<Double> rating1;
//        rating1 = Optional.of(3.3);
//        Optional<Double> rating2;
//        rating2 = Optional.of(3.2);
//        List<User> users = new ArrayList<>();
//        users.add(first);
//        users.add(second);


        //Act
//        when(userRepository.findAllByIsDeletedIsFalse()).thenReturn(users);
//        when(ratingRepository.findAverageRatingByUserAsPassenger(1L)).thenReturn(rating1);
//        when(ratingRepository.findAverageRatingByUserAsDriver(1L)).thenReturn(rating2);

        //Assert
//        Assert.assertEquals(users, userService.getUsers());
//    }

//    @Test
//    public void getUser() {
//        User first = new User("firstName", "lastName", "username1",
//                "email@gmail.com", UserRole.USER, "password", "phone", 3.5,
//                4.0);
//        first.setModelId(1L);
//        Optional<Double> rating1;
//        rating1 = Optional.of(3.3);
//        Optional<Double> rating2;
//        rating2 = Optional.of(3.2);
//        when(userRepository.findFirstByUsername("username1")).thenReturn(first);
//        when(ratingRepository.findAverageRatingByUserAsPassenger(1L)).thenReturn(rating1);
//        when(ratingRepository.findAverageRatingByUserAsDriver(1L)).thenReturn(rating2);
//
//        Assert.assertEquals(first, userService.getUser("username1"));
//    }

//    @Test
//    public void save() {
//        User user = new User("firstName", "lastName", "username1",
//                "email@gmail.com", "0888999888", 3.2, 3.3,
//                "Password!1", "USER", null, "URI", null,
//                null, null, null, null);
//        User newUser = new User();
//        newUser.setUsername(user.getUsername());
//        newUser.setFirstName(user.getFirstName());
//        newUser.setLastName(user.getLastName());
//        newUser.setPassword(bCryptEncoder.encode(user.getPassword()));
//        newUser.setEmail(user.getEmail());
//        newUser.setRole("USER");
//        newUser.setIsDeleted(false);
//        newUser.setPhone(user.getPhone());
//        when(userRepository.save(user)).thenReturn(newUser);
//
//        Assert.assertEquals(newUser, userService.save(user));
//    }


//    @Test
//    public void updatePassword_Should_callRepository_when_UserHasAuthority() {
//        //Arrange
//        User toBeEdited = new User("firstName", "lastName", "username1",
//                "email@gmail.com", UserRole.USER, "password", "phone", 3.5,
//                4.0);
//        toBeEdited.setModelId(2L);
//
//        User currentUser = new User("firstName", "lastName", "username1",
//                "email@gmail.com", UserRole.USER, "password", "phone", 3.5,
//                4.0);
//        currentUser.setModelId(1L);
//        currentUser.setRole(UserRole.USER);
//
//        //Act
//        when(userRepository.findById(2L)).thenReturn(Optional.of(toBeEdited));
////        userService.updateCurrentUserPassword("newPassword",currentUser);
//
//        //Assert
////        verify(userService, times(1)).updateCurrentUserPassword("newPassword",currentUser);
//    }

//    @Test
//    public void updateEmail_Should_callRepository_when_UserHasAuthority() {
//        //Arrange
//        User toBeEdited = new User("firstName", "lastName", "username1",
//                "email@gmail.com", UserRole.USER, "password", "phone", 3.5,
//                4.0);
//        toBeEdited.setModelId(2L);
//
//        User currentUser = new User("firstName", "lastName", "username1",
//                "email@gmail.com", UserRole.USER, "password", "phone", 3.5,
//                4.0);
//        currentUser.setModelId(1L);
////        currentUser.setRole("USER");
//
//        //Act
//        when(userRepository.findById(2L)).thenReturn(Optional.of(toBeEdited));
////        userService.updateCurrentUserEmail("newEmail@gmail.com",currentUser);
//
//        //Assert
////        verify(userService, times(1)).updateCurrentUserEmail("newEmail@gmail.com",currentUser);
//    }


}

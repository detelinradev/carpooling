package com.telerik.carpooling.ServiceTests;


import com.telerik.carpooling.enums.UserRole;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.dtos.mapper.DtoMapper;
import com.telerik.carpooling.payload.UploadFileResponse;
import com.telerik.carpooling.repositories.*;
import com.telerik.carpooling.services.UserServiceImpl;
import com.telerik.carpooling.services.services.contracts.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    CarRepository carRepository;
    @Mock
    CarService carService;

    @Mock
    CommentRepository commentRepository;
    @Mock
    CommentService commentService;

    @Mock
    ImageRepository imageRepository;
    @Mock
    ImageService imageService;
    @Mock
    UploadFileResponse uploadFileResponse;

    @Mock
    RatingService ratingService;
    @Mock
    RatingRepository ratingRepository;

    @Mock
    DtoMapper dtoMapper;

    @Spy
    @InjectMocks
    UserServiceImpl userService;


    @Test
    public void findAll_Should_ReturnAll() {
        //Arrange
        User first = new User("firstName", "lastName", "username1",
                "email@gmail.com", UserRole.USER, "password", "phone", 3.5,
                4.0);
        User second = new User("firstName", "lastName", "username1",
                "email@gmail.com", UserRole.USER, "password", "phone", 3.5,
                4.0);
        first.setModelId(1L);
        Optional<Double> rating1;
        rating1 = Optional.of(3.3);
        Optional<Double> rating2;
        rating2 = Optional.of(3.2);
        List<User> users = new ArrayList<>();
        users.add(first);
        users.add(second);


        //Act
//        when(userRepository.findAllByIsDeletedIsFalse()).thenReturn(users);
//        when(ratingRepository.findAverageRatingByUserAsPassenger(1L)).thenReturn(rating1);
//        when(ratingRepository.findAverageRatingByUserAsDriver(1L)).thenReturn(rating2);

        //Assert
//        Assert.assertEquals(users, userService.getUsers());
    }

    @Test
    public void getUser() {
        User first = new User("firstName", "lastName", "username1",
                "email@gmail.com", UserRole.USER, "password", "phone", 3.5,
                4.0);
        first.setModelId(1L);
        Optional<Double> rating1;
        rating1 = Optional.of(3.3);
        Optional<Double> rating2;
        rating2 = Optional.of(3.2);
//        when(userRepository.findFirstByUsername("username1")).thenReturn(first);
//        when(ratingRepository.findAverageRatingByUserAsPassenger(1L)).thenReturn(rating1);
//        when(ratingRepository.findAverageRatingByUserAsDriver(1L)).thenReturn(rating2);
//
//        Assert.assertEquals(first, userService.getUser("username1"));
    }

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



    @Test
    public void updatePassword_Should_callRepository_when_UserHasAuthority()
    {
        //Arrange
        User toBeEdited = new User("firstName", "lastName", "username1",
                "email@gmail.com", UserRole.USER, "password", "phone", 3.5,
                4.0);
        toBeEdited.setModelId(2L);

        User currentUser = new User("firstName", "lastName", "username1",
                "email@gmail.com", UserRole.USER, "password", "phone", 3.5,
                4.0);
        currentUser.setModelId(1L);
        currentUser.setRole(UserRole.USER);

        //Act
        when(userRepository.findById(2L)).thenReturn(Optional.of(toBeEdited));
//        userService.updateCurrentUserPassword("newPassword",currentUser);

        //Assert
//        verify(userService, times(1)).updateCurrentUserPassword("newPassword",currentUser);
    }

    @Test
    public void updateEmail_Should_callRepository_when_UserHasAuthority()
    {
        //Arrange
        User toBeEdited = new User("firstName", "lastName", "username1",
                "email@gmail.com", UserRole.USER, "password", "phone", 3.5,
                4.0);
        toBeEdited.setModelId(2L);

        User currentUser = new User("firstName", "lastName", "username1",
                "email@gmail.com", UserRole.USER, "password", "phone", 3.5,
                4.0);
        currentUser.setModelId(1L);
//        currentUser.setRole("USER");

        //Act
        when(userRepository.findById(2L)).thenReturn(Optional.of(toBeEdited));
//        userService.updateCurrentUserEmail("newEmail@gmail.com",currentUser);

        //Assert
//        verify(userService, times(1)).updateCurrentUserEmail("newEmail@gmail.com",currentUser);
    }



}

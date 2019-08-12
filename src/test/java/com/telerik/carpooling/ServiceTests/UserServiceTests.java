package com.telerik.carpooling.ServiceTests;


import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.UserDtoRequest;
import com.telerik.carpooling.models.dtos.UserDtoResponse;
import com.telerik.carpooling.models.dtos.dtos.mapper.DtoMapper;
import com.telerik.carpooling.payload.UploadFileResponse;
import com.telerik.carpooling.repositories.*;
import com.telerik.carpooling.services.UserServiceImpl;
import com.telerik.carpooling.services.services.contracts.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;

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
    ModelMapper modelMapper;
    @Mock
    DtoMapper dtoMapper;

    @Mock
    UserService userService;


    @Test
    public void findAll_Should_ReturnAll() {
        //Arrange
        User first = new User("firstName", "lastName", "username1",
                "email@gmail.com", "0888999888", 3.2, 3.3,
                "Password!1", "USER", null, "URI", null,
                null, null, null, null);
        UserDtoRequest req = new UserDtoRequest();
        req.setUsername("username1");
        UserDtoResponse res = new UserDtoResponse();
        res.setUsername("username1");
        User second = new User("firstNamesaf", "lastNamefsa", "username2",
                "email@gmail.com", "0888999888", 3.2, 3.3,
                "Password!1", "USER", null, "URI", null,
                null, null, null, null);
        List<UserDtoResponse> users = new ArrayList<>();
//        users.add(req);
//        users.add(res);

        //Act
        when(userService.getUser("username1")).thenReturn(first);
//        when(userRepository.findAllByIsDeletedIsFalse()).thenReturn(users);
//        when(userService.getUserOwnTrips("username1")).thenReturn(null);

        //Assert
        Assert.assertEquals(users, userService.getUsers());
    }

    @Test
    public void safa() {
    }
}

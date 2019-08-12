package com.telerik.carpooling.ServiceTests;


import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.models.*;
import com.telerik.carpooling.models.dtos.TripDtoRequest;
import com.telerik.carpooling.models.dtos.UserDtoRequest;
import com.telerik.carpooling.models.dtos.dtos.mapper.DtoMapper;
import com.telerik.carpooling.payload.UploadFileResponse;
import com.telerik.carpooling.repositories.*;
import com.telerik.carpooling.services.TripServiceImpl;
import com.telerik.carpooling.services.services.contracts.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class TripServiceTests {
    @Mock
    TripRepository tripRepository;
//    @Mock
//    TripService tripService;

    @Mock
    UserRepository userRepository;
    @Mock
    UserService userService;

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


    private Trip trip;
    private User user;
    private Car car;
    private Image image;
    private Image carImage;
    private Comment comment;
    private Rating ratingAsDriver;
    private Rating ratingAsPassenger;


    @Spy
    @InjectMocks
    TripServiceImpl tripService;

    @Before
    public void SetUp() {
        MockitoAnnotations.initMocks(this);
        byte[] content = new byte[20];
        new Random().nextBytes(content);
        this.user = new User("firstName", "lastName", "username1",
                "email@gmail.com", "0888999888", 3.2, 3.3,
                "Password!1", "USER", null, "URI", new ArrayList<>(),
                null, null, image, car);

        this.image = new Image("fileName", "picture", content, user);
        this.image.setModelId(1L);

        this.car = new Car("model", "brand", "color", 2018, "yes", carImage, user, null);
        this.carImage = new Image("fileName", "picture", content, car);
        this.image.setModelId(2L);
        this.trip = new Trip("Sofia",
                "Burgas",
                LocalDateTime.of(2019, 10, 5, 10, 22),
                3,
                3,
                3,
                "message",
                TripStatus.AVAILABLE,
                "no",
                "no",
                "no",
                new ArrayList<>(),
                null,
                user,
                car,
                null,
                null,
                null,
                null,
                null,
                null,
                null);


    }

    @Test
    public void find_Should_ReturnAll() {
        //Arrange
        List<Trip> trips = new ArrayList<>();
        Trip trip2 = new Trip("Sofia", "Burgas", LocalDateTime.now(),
                3, 3, 3, "message", null, "no", "no", "no", userRepository.findAll(),
                null, null, null, null, null, null, null, null, null, null);
        trips.add(trip);
        trips.add(trip2);


        //Act
        when(trips.get(0)).thenReturn(trip);
        tripService.getTrip("1");
        //Assert
        Assert.assertEquals(trips, tripService.getTrips(null, null, null, null, null, null, null, null, null, null, null, null));
//                tripService.getTrips(null,null,null,null,null,null,null,null,null,null,null,null
    }


}

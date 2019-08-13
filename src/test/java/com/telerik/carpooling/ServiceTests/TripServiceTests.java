package com.telerik.carpooling.ServiceTests;


import com.telerik.carpooling.enums.PassengerStatus;
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
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.mockito.Mockito.verify;
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
    private Trip trip2;
    private User user;
    private Car car;
    private Image image;
    private Image carImage;
    private Comment comment;
    private Rating ratingAsDriver;
    private Rating ratingAsPassenger;
    private List<Trip> trips;


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
        trips = new ArrayList<>();
        trip2 = new Trip("Sofia", "Burgas", LocalDateTime.now(),
                3, 3, 3, "message", null, "no", "no", "no", userRepository.findAll(),
                null, null, null, null, null, null, null, null, null, null);
        trip.setModelId(1L);
        trips.add(trip);
        trips.add(trip2);


    }

    @Test
    public void find_Should_ReturnAll() {
        //Act

        when(userRepository.findFirstByUsername("username1")).thenReturn(user);
        when(tripService.getTrips(null, null, null, null, null, null, null, null, null, null, null, null)).thenReturn(trips);
        tripService.getTrips(null, null, null, null, null, null, null, null, null, null, null, null);
        //Assert
        Assert.assertEquals(trips, tripService.getTrips(null, null, null, null, null, null, null, null, null, null, null, null));
    }

    @Test
    public void create_trip_Should_CreateNewTrip() {

        //Act
        user.setCar(car);
        car.setOwner(user);
        when(userService.getUser("username1")).thenReturn(user);
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        //Assert
        Assert.assertEquals(trip, tripService.createTrip(trip, user));
    }

//    @Test (expected = NullPointerException.class)
//    public void create_trip_Should_ThrowException_IfNoCarIsPresent() {
//
//user.setCar(null);
//
//        Mockito.when(userService.getUser("username1")).thenReturn(user);
//        Mockito.when(carService.getCar(user)).thenReturn(null);
//        tripService.createTrip(trip, user);
////        verify(tripService, Mockito.times(0)).createTrip(trip, user);
//
//    }

    @Test(expected = NullPointerException.class)
    public void changeTripStatus_Should_ThrowException_When_NotPresent() {
        when(tripService.changeTripStatus("1", user, TripStatus.BOOKED)).thenReturn(null);
        tripService.changeTripStatus("1", user, TripStatus.BOOKED);
    }

    @Test(expected = NullPointerException.class)
    public void AddPassenger_Should_ThrowException_When_NotPresent() {
        when(tripService.addPassenger("1", user)).thenReturn(null);


        Assert.assertEquals(trip, tripService.addPassenger("1", user));
    }
//
//    @Test
//    public void AddPassenger_Should_When_NotPresent() {
//        long longid = Long.parseLong("1");
//        when(tripRepository.findById(longid)).thenReturn(Optional.of(trip));
//        when(tripRepository.save(trip)).thenReturn(trip);
//        when(userRepository.save(user)).thenReturn(user);
////        when(tripService.parseStringToLong("1")).thenReturn(1L);
//
//        Assert.assertEquals(trip, tripService.addPassenger("1", user));
//    }

//    @Test
//    public void _Should_When_NotPresent() {
//        User passenger = new User();
//        passenger.setModelId(1L);
//        tripService.addPassenger("1", passenger);
////        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
////        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//        when(tripService.changePassengerStatus("1", user, "2", PassengerStatus.CANCELED)).thenReturn(trip);
//        Assert.assertEquals(trip, tripService.changePassengerStatus("1", user, "2", PassengerStatus.CANCELED));
//    }




}

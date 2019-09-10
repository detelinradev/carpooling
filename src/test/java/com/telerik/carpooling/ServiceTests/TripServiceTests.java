package com.telerik.carpooling.ServiceTests;


import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.enums.UserRole;
import com.telerik.carpooling.models.*;
import com.telerik.carpooling.models.dtos.TripDtoRequest;
import com.telerik.carpooling.models.dtos.TripDtoResponse;
import com.telerik.carpooling.models.dtos.dtos.mapper.DtoMapper;
import com.telerik.carpooling.repositories.*;
import com.telerik.carpooling.services.TripServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import javassist.NotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class TripServiceTests {

    @Mock
    TripRepository tripRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    CarRepository carRepository;

    @Mock
    TripUserStatusRepository tripUserStatusRepository;

    @Mock
    DtoMapper dtoMapper;


    private Trip trip;
    private Trip trip2;
    private TripDtoResponse tripDtoResponse;
    private TripDtoResponse tripDtoResponse2;
    private TripDtoRequest tripDtoRequest;
    private User user;
    private Car car;
    private Image image;
    private Image carImage;
    private List<Trip> trips;
    private List<TripDtoResponse> tripDtoResponses;


    @Spy
    @InjectMocks
    TripServiceImpl tripService;

    @Before
    public void SetUp() {
//        MockitoAnnotations.initMocks(this);
        byte[] content = new byte[20];
        new Random().nextBytes(content);
        user = new User("username1", "lastName", "username1",
                "email@gmail.com", UserRole.USER, "password", "phone", 3.5,
                4.0);
        user.setModelId(1L);

        image = new Image("fileName", "picture", content, user);
        image.setModelId(1L);

        car = new Car("model", "brand", "color", 2018, user);
        carImage = new Image("fileName", "picture", content, car);
        image.setModelId(2L);

        trip = new Trip("message", LocalDateTime.MAX,
                "origin", "destination", 3, 5,4,
                true, true,true,true,TripStatus.AVAILABLE);
        trip2 = new Trip("message", LocalDateTime.MAX,
                "origin", "destination", 3, 5,4,
                true, true,true,true,TripStatus.AVAILABLE);
        tripDtoResponse = new TripDtoResponse(1L, "message", LocalDateTime.MAX,
                "origin","destination", 3, TripStatus.AVAILABLE, 4,
                4, true, true, true,true);
        tripDtoResponse2 = new TripDtoResponse(2L, "message", LocalDateTime.MAX,
                "origin","destination", 3, TripStatus.AVAILABLE, 4,
                4, true, true, true,true);
        tripDtoRequest = new TripDtoRequest("message", LocalDateTime.MAX,
                "origin","destination", 3,5,
                4, true, true, true,true);
        trips = new ArrayList<>();
        trips.add(trip);
        trips.add(trip2);
        tripDtoResponses = new ArrayList<>();
        tripDtoResponses.add(tripDtoResponse);
        tripDtoResponses.add(tripDtoResponse2);
    }

    @Test
    public void create_trip_Should_CreateNewTrip() throws NotFoundException {
        when(carRepository.findByOwnerAndIsDeletedFalse(user)).thenReturn(Optional.of(car));
        when(tripRepository.save(trip)).thenReturn(trip);
        when(dtoMapper.dtoToObject(tripDtoRequest)).thenReturn(trip);
        when(dtoMapper.objectToDto(trip)).thenReturn(tripDtoResponse);
        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.of(user));

        Assert.assertEquals(tripDtoResponse,tripService.createTrip(tripDtoRequest,"username1"));
    }

    @Test (expected = NotFoundException.class)
    public void create_trip_Should_ThrowException_IfNoCarIsPresent() throws NotFoundException {

        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.of(user));
        when(carRepository.findByOwnerAndIsDeletedFalse(user)).thenReturn(Optional.empty());
        tripService.createTrip(tripDtoRequest, "username1");

    }

    @Test(expected = NullPointerException.class)
    public void changeTripStatus_Should_ThrowException_When_NotPresent() throws NotFoundException {
//        when(tripService.changeTripStatus(1L, "username1", TripStatus.BOOKED)).thenReturn(null);
        tripService.changeTripStatus(1L, "username1", TripStatus.BOOKED);
    }
}

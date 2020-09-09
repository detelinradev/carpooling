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
import com.telerik.carpooling.repository.CarRepository;
import com.telerik.carpooling.repository.TripRepository;
import com.telerik.carpooling.repository.TripUserStatusRepository;
import com.telerik.carpooling.repository.UserRepository;
import com.telerik.carpooling.service.TripUserServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class TripUserServiceTests {

    @Mock
    TripRepository tripRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    TripStatus tripStatus;

    @Mock
    CarRepository carRepository;

    @Mock
    TripUserStatusRepository tripUserStatusRepository;

    @Mock
    DtoMapper dtoMapper;


    private Trip trip;
    private TripDtoResponse tripDtoResponse;
    private TripDtoEdit tripDtoEdit;
    private TripDtoRequest tripDtoRequest;
    private TripUserStatus tripUserStatusPassenger;
    private TripUserStatus tripUserStatusDriver;
    private TripUserStatusDtoResponse tripUserStatusDtoResponse;
    private User user;
    private UserDtoResponse userDtoResponse;
    private Car car;


    @Spy
    @InjectMocks
    TripUserServiceImpl tripUserService;

    @Before
    public void SetUp() {

        user = new User("username1", "lastName", "username1",
                "email@gmail.com", UserRole.USER, "password", "phone", 3.5,
                4.0);
        user.setModelId(1L);
        userDtoResponse = new UserDtoResponse(1L,"username1","lastName","username1",
                "email@gmail.com",UserRole.USER, "phone",3.5,4.0);
        car = new Car("model", "brand", "color", 2018, true, user);
        trip = new Trip("message", LocalDateTime.MAX,
                "origin", "destination", 3, 5, 4,
                true, true, true, true, TripStatus.AVAILABLE);
        trip.setModelId(1L);
        tripDtoResponse = new TripDtoResponse(1L, "message", LocalDateTime.MAX,
                "origin", "destination", 3, TripStatus.AVAILABLE, 5,
                4, true, true, true, true);
        tripDtoRequest = new TripDtoRequest("message", LocalDateTime.MAX,
                "origin", "destination", 3, 5,
                4, true, true, true, true);
        tripDtoEdit = new TripDtoEdit(1L, "message", LocalDateTime.MAX,
                "origin", "destination", 3, 5,
                4, true, true, true, true);
        tripUserStatusPassenger = new TripUserStatus(user, trip, UserStatus.PENDING);
        tripUserStatusDriver = new TripUserStatus(user, trip, UserStatus.DRIVER);
        tripUserStatusDtoResponse = new TripUserStatusDtoResponse(1L,tripDtoResponse, userDtoResponse, UserStatus.DRIVER);

    }

    @Test
    public void create_tripUserStatus_Should_CreateNewTripUserStatus_WhenUsernameReturnsUser(){

        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(user));
        when(tripUserStatusRepository.save(tripUserStatusDriver)).thenReturn(tripUserStatusDriver);
        when(dtoMapper.objectToDtoTrip(tripUserStatusDriver)).thenReturn(tripUserStatusDtoResponse);

        Assert.assertEquals(tripUserStatusDtoResponse, tripUserService.createTripUserStatusAsDriver(trip, "username1"));
    }

    @Test (expected = UsernameNotFoundException.class)
    public void create_tripUserStatus_Should_ThrowException_IfUsernameDoNotReturnUser(){

        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.empty());

        tripUserService.createTripUserStatusAsDriver(trip, "username1");
    }
}

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
import com.telerik.carpooling.service.TripUserStatusServiceImpl;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
    private TripUserStatusDtoResponse tripUserStatusDtoResponseDriver;
    private TripUserStatusDtoResponse tripUserStatusDtoResponsePassenger;
    private User user;
    private UserDtoResponse userDtoResponse;
    private Car car;
    private List<TripUserStatus> tripUserStatusList;
    private List<TripUserStatusDtoResponse> tripUserStatusDtoResponsesList;


    @Spy
    @InjectMocks
    TripUserStatusServiceImpl tripUserService;

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
        tripUserStatusDtoResponseDriver = new TripUserStatusDtoResponse(1L,tripDtoResponse, userDtoResponse,
                UserStatus.DRIVER);
        tripUserStatusDtoResponsePassenger = new TripUserStatusDtoResponse(2L,tripDtoResponse, userDtoResponse,
                UserStatus.PENDING);
        tripUserStatusList = new ArrayList<>();
        tripUserStatusList.add(tripUserStatusDriver);
        tripUserStatusList.add(tripUserStatusPassenger);
        tripUserStatusDtoResponsesList = new ArrayList<>();
        tripUserStatusDtoResponsesList.add(tripUserStatusDtoResponseDriver);
        tripUserStatusDtoResponsesList.add(tripUserStatusDtoResponsePassenger);


    }

    @Test
    public void create_tripUserStatus_Should_CreateNewTripUserStatus_WhenUsernameReturnsUser(){

        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(user));
        when(tripUserStatusRepository.save(tripUserStatusDriver)).thenReturn(tripUserStatusDriver);
        when(dtoMapper.objectToDtoTrip(tripUserStatusDriver)).thenReturn(tripUserStatusDtoResponseDriver);

        Assert.assertEquals(tripUserStatusDtoResponseDriver, tripUserService.createTripUserStatusAsDriver(trip, "username1"));
    }

    @Test (expected = UsernameNotFoundException.class)
    public void create_tripUserStatus_Should_ThrowException_IfUsernameDoNotReturnUser(){

        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.empty());

        tripUserService.createTripUserStatusAsDriver(trip, "username1");
    }

    @Test
    public void get_userOwnTripsWithDrivers_Should_ReturnListWithResults_WhenUsernameIsProvided(){

        when(tripUserStatusRepository.findAllUserOwnTripsWithDrivers("username1")).thenReturn(tripUserStatusList);
        when(dtoMapper.tripUserStatusToDtoList(tripUserStatusList)).thenReturn(tripUserStatusDtoResponsesList);

        Assert.assertEquals(tripUserStatusDtoResponsesList,tripUserService.getUserOwnTripsWithDrivers("username1"));
    }

    @Test
    public void get_userOwnTripsWithDrivers_Should_ReturnEmptyList_WhenUsernameIsNotProvided(){

        when(tripUserStatusRepository.findAllUserOwnTripsWithDrivers("")).thenReturn(Collections.emptyList());
        when(dtoMapper.tripUserStatusToDtoList(Collections.emptyList())).thenReturn(Collections.emptyList());

        Assert.assertEquals(Collections.emptyList(),tripUserService.getUserOwnTripsWithDrivers(""));
    }

    @Test
    public void get_userOwnTripsWithDrivers_Should_ReturnEmptyList_WhenUsernameIsNull(){

        when(tripUserStatusRepository.findAllUserOwnTripsWithDrivers(null)).thenReturn(Collections.emptyList());
        when(dtoMapper.tripUserStatusToDtoList(Collections.emptyList())).thenReturn(Collections.emptyList());

        Assert.assertEquals(Collections.emptyList(), tripUserService.getUserOwnTripsWithDrivers(null));
    }
}

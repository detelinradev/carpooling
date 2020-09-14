package com.telerik.carpooling.serviceTests;


import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.enums.UserRole;
import com.telerik.carpooling.enums.UserStatus;
import com.telerik.carpooling.exception.MyNotFoundException;
import com.telerik.carpooling.model.Car;
import com.telerik.carpooling.model.Trip;
import com.telerik.carpooling.model.TripUserStatus;
import com.telerik.carpooling.model.User;
import com.telerik.carpooling.model.dto.TripDtoEdit;
import com.telerik.carpooling.model.dto.TripDtoRequest;
import com.telerik.carpooling.model.dto.TripDtoResponse;
import com.telerik.carpooling.model.dto.dto.mapper.DtoMapper;
import com.telerik.carpooling.repository.CarRepository;
import com.telerik.carpooling.repository.TripRepository;
import com.telerik.carpooling.repository.TripUserStatusRepository;
import com.telerik.carpooling.service.TripServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class TripServiceTests {

    @Mock
    TripRepository tripRepository;

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
    private TripUserStatus tripUserStatus;
    private User user;
    private Car car;
    private List<TripUserStatus> tripUserStatusList;


    @Spy
    @InjectMocks
    TripServiceImpl tripService;

    @Before
    public void SetUp() {

        user = new User("username1", "lastName", "username1",
                "email@gmail.com", UserRole.USER, "password", "phone", 3.5,
                4.0,3, 4.0, 3, 4.0);
        user.setModelId(1L);
        car = new Car("model", "brand", "color", 2018,true, user);
        trip = new Trip("message", LocalDateTime.MAX,
                "origin", "destination", 3, 5,4,
                true, true,true,true,TripStatus.AVAILABLE);
        trip.setModelId(1L);
        tripDtoResponse = new TripDtoResponse(1L, "message", LocalDateTime.MAX,
                "origin","destination", 3, TripStatus.AVAILABLE, 4,
                4, true, true, true,true);
        tripDtoRequest = new TripDtoRequest("message", LocalDateTime.MAX,
                "origin","destination", 3,5,
                4, true, true, true,true);
        tripDtoEdit = new TripDtoEdit(1L, "message", LocalDateTime.MAX,
                "origin","destination", 3, 4,
                4, true, true, true,true);
        tripUserStatus = new TripUserStatus(user,trip, UserStatus.PENDING);
        tripUserStatusList = new ArrayList<>();
        tripUserStatusList.add(tripUserStatus);
    }

    @Test
    public void create_trip_Should_CreateNewTrip_WhenCarIsPresent() throws MyNotFoundException {

        when(carRepository.findByOwnerAndIsDeletedFalse(user.getUsername())).thenReturn(Optional.of(car));
        when(tripRepository.save(trip)).thenReturn(trip);
        when(dtoMapper.dtoToObject(tripDtoRequest)).thenReturn(trip);

        Assert.assertEquals(trip,tripService.createTrip(tripDtoRequest,"username1"));
    }

    @Test (expected = MyNotFoundException.class)
    public void create_trip_Should_ThrowException_IfNoCarIsPresent() throws MyNotFoundException {

        when(carRepository.findByOwnerAndIsDeletedFalse(user.getUsername())).thenReturn(Optional.empty());

        tripService.createTrip(tripDtoRequest, "username1");
    }

    @Test
    public void update_trip_Should_updateTrip_WhenLoggedUserIsOwnerOfTrip() throws MyNotFoundException {

        when(tripUserStatusRepository.findFirstByTripModelIdAndUserUsernameAsDriver(1L,"username1"))
        .thenReturn(Optional.ofNullable(tripUserStatus));
        when(dtoMapper.dtoToObject(tripDtoEdit)).thenReturn(trip);
        when(dtoMapper.objectToDto(trip)).thenReturn(tripDtoResponse);
        when(tripRepository.save(trip)).thenReturn(trip);

        Assert.assertEquals(tripDtoResponse,tripService.updateTrip(tripDtoEdit,"username1"));
    }

    @Test (expected = IllegalArgumentException.class)
    public void update_trip_Should_ThrowException_IfLoggedUserIsNotOwnerOfTrip() {

        when(tripUserStatusRepository.findFirstByTripModelIdAndUserUsernameAsDriver(1L,"username1"))
                .thenReturn(Optional.empty());

        tripService.updateTrip(tripDtoEdit, "username1");
    }

    @Test
    public void delete_trip_Should_deleteTrip_WhenLoggedUserIsOwnerOfTripOrAdminAndTripModelIdIsValid() {

        when(tripUserStatusRepository.findOneByTripModelIdAndUserAsDriverOrAdmin(1L,"username1"))
                .thenReturn(Optional.ofNullable(tripUserStatus));
        when(tripRepository.findByModelIdAndIsDeletedFalse(1L)).thenReturn(Optional.ofNullable(trip));
        when( tripUserStatusRepository.findAllByTripModelIdAndIsDeletedFalse(1L))
                .thenReturn(tripUserStatusList);
        when(tripRepository.save(trip)).thenReturn(trip);
        when(tripUserStatusRepository.save(tripUserStatus)).thenReturn(tripUserStatus);

        tripService.deleteTrip(1L,"username1");

        verify(tripUserStatusRepository,times(1))
                .findOneByTripModelIdAndUserAsDriverOrAdmin(1L,"username1");
        verify(tripUserStatusRepository,times(1))
                .findAllByTripModelIdAndIsDeletedFalse(1L);
        verify(tripRepository,times(1)).save(trip);
        verify(tripRepository,times(1)).findByModelIdAndIsDeletedFalse(1L);
        verify(tripUserStatusRepository,times(1)).save(tripUserStatus);

        verifyNoMoreInteractions(tripRepository,tripUserStatusRepository);
    }

    @Test (expected = IllegalArgumentException.class)
    public void delete_trip_Should_ThrowException_IfLoggedUserIsNotOwnerOfTrip() {

        when(tripUserStatusRepository.findFirstByTripModelIdAndUserUsernameAsDriver(1L,"username1"))
                .thenReturn(Optional.empty());

        tripService.deleteTrip(1L, "username1");
    }

    @Test (expected = IllegalArgumentException.class)
    public void delete_trip_Should_ThrowException_IfTripModelIdIsNotValid() {

        when(tripRepository.findByModelIdAndIsDeletedFalse(1L))
                .thenReturn(Optional.empty());

        tripService.deleteTrip(1L, "username1");
    }

    @Test
    public void change_TripStatus_Should_changeTripStatus_WhenLoggedUserIsOwnerOfTripAndTripModelIdIsValid() {

        when(tripUserStatusRepository.findFirstByTripModelIdAndUserUsernameAsDriver(1L,"username1"))
                .thenReturn(Optional.ofNullable(tripUserStatus));
        when(tripRepository.findByModelIdAndIsDeletedFalse(1L)).thenReturn(Optional.ofNullable(trip));
        when(tripStatus.changeTripStatus(trip,tripService)).thenReturn(trip);
        when(tripRepository.save(trip)).thenReturn(trip);

        tripService.changeTripStatus(1L,"username1",tripStatus);

        verify(tripUserStatusRepository,times(1))
                .findFirstByTripModelIdAndUserUsernameAsDriver(1L,"username1");
        verify(tripRepository,times(1)).save(trip);
        verify(tripRepository,times(1)).findByModelIdAndIsDeletedFalse(1L);
        verify(tripStatus,times(1)).changeTripStatus(trip,tripService);

        verifyNoMoreInteractions(tripRepository,tripUserStatusRepository,tripStatus);
    }

    @Test (expected = IllegalArgumentException.class)
    public void change_tripStatus_Should_ThrowException_IfTripModelIdIsNotValid() {

        when(tripRepository.findByModelIdAndIsDeletedFalse(1L))
                .thenReturn(Optional.empty());

        tripService.changeTripStatus(1L, "username1", tripStatus);
    }

    @Test (expected = IllegalArgumentException.class)
    public void change_tripStatus_Should_ThrowException_IfLoggedUserIsNotOwnerOfTrip() {

        when(tripUserStatusRepository.findFirstByTripModelIdAndUserUsernameAsDriver(1L,"username1"))
                .thenReturn(Optional.empty());

        tripService.changeTripStatus(1L, "username1", tripStatus);
    }

    @Test
    public void change_Pending_UserStatusesToRejected_Should_Succeed_WhenLoggedUserIsOwnerOfTripAndTripModelIdIsValid() {

        TripUserStatus tripUserStatusResponse = new TripUserStatus(user,trip,UserStatus.REJECTED);
        when(tripUserStatusRepository.findAllByTripModelIdAndIsDeletedFalse(1L))
                .thenReturn(Collections.singletonList(tripUserStatus));
        when(tripUserStatusRepository.save(tripUserStatusResponse))
                .thenReturn(tripUserStatusResponse);


        tripService.changeAllLeftPendingUserStatusesToRejected(trip);

        verify(tripUserStatusRepository,times(1))
                .findAllByTripModelIdAndIsDeletedFalse(1L);
        verify(tripUserStatusRepository,times(1)).save(tripUserStatusResponse);

        verifyNoMoreInteractions(tripUserStatusRepository);
    }
}

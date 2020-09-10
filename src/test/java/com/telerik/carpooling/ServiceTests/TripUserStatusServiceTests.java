package com.telerik.carpooling.ServiceTests;


import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.enums.UserRole;
import com.telerik.carpooling.enums.UserStatus;
import com.telerik.carpooling.model.Trip;
import com.telerik.carpooling.model.TripUserStatus;
import com.telerik.carpooling.model.User;
import com.telerik.carpooling.model.dto.TripDtoResponse;
import com.telerik.carpooling.model.dto.TripUserStatusDtoResponse;
import com.telerik.carpooling.model.dto.UserDtoResponse;
import com.telerik.carpooling.model.dto.dto.mapper.DtoMapper;
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
import org.springframework.data.domain.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class TripUserStatusServiceTests {

    @Mock
    TripRepository tripRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    TripUserStatusRepository tripUserStatusRepository;

    @Mock
    UserStatus userStatus;

    @Mock
    DtoMapper dtoMapper;

    private Trip trip;
    private TripUserStatus tripUserStatusPassenger;
    private TripUserStatus tripUserStatusDriver;
    private TripUserStatusDtoResponse tripUserStatusDtoResponseDriver;
    private User userDriver;
    private User userPassenger;
    private List<TripUserStatus> tripUserStatusList;
    private List<TripUserStatusDtoResponse> tripUserStatusDtoResponsesList;
    private Slice<TripUserStatus> tripUserStatusSlice;
    private Pageable pageable;
    private LocalDateTime earliestDepartureTime;
    private LocalDateTime latestDepartureTime;

    @Spy
    @InjectMocks
    TripUserStatusServiceImpl tripUserService;

    @Before
    public void SetUp() {

        userDriver = new User("username1", "lastName", "username1",
                "email@gmail.com", UserRole.USER, "password", "phone", 3.5,
                4.0);
        userDriver.setModelId(1L);
        userPassenger = new User("username2", "lastName", "username2",
                "email@gmail.com", UserRole.USER, "password", "phone", 3.5,
                4.0);
        userPassenger.setModelId(2L);
        UserDtoResponse userDtoResponse = new UserDtoResponse(1L, "username1", "lastName", "username1",
                "email@gmail.com", UserRole.USER, "phone", 3.5, 4.0);
        trip = new Trip("message", LocalDateTime.MAX,
                "origin", "destination", 1, 5, 4,
                true, true, true, true, TripStatus.AVAILABLE);
        trip.setModelId(1L);
        TripDtoResponse tripDtoResponse = new TripDtoResponse(1L, "message", LocalDateTime.MAX,
                "origin", "destination", 3, TripStatus.AVAILABLE, 5,
                4, true, true, true, true);
        tripUserStatusPassenger = new TripUserStatus(userPassenger, trip, UserStatus.PENDING);
        tripUserStatusDriver = new TripUserStatus(userDriver, trip, UserStatus.DRIVER);
        tripUserStatusDtoResponseDriver = new TripUserStatusDtoResponse(1L, tripDtoResponse, userDtoResponse,
                UserStatus.DRIVER);
        TripUserStatusDtoResponse tripUserStatusDtoResponsePassenger = new TripUserStatusDtoResponse(2L, tripDtoResponse, userDtoResponse,
                UserStatus.PENDING);
        tripUserStatusList = new ArrayList<>();
        tripUserStatusList.add(tripUserStatusDriver);
        tripUserStatusList.add(tripUserStatusPassenger);
        tripUserStatusSlice = new SliceImpl<>(tripUserStatusList);
        pageable = PageRequest.of(0, 10, Sort.by("modified").descending());
        tripUserStatusDtoResponsesList = new ArrayList<>();
        tripUserStatusDtoResponsesList.add(tripUserStatusDtoResponseDriver);
        tripUserStatusDtoResponsesList.add(tripUserStatusDtoResponsePassenger);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        earliestDepartureTime = LocalDateTime.parse("2020-11-30 12:00", dateTimeFormatter);
        latestDepartureTime = LocalDateTime.parse("2020-12-30 12:00", dateTimeFormatter);
    }

    @Test
    public void create_tripUserStatus_Should_CreateNewTripUserStatus_WhenUsernameReturnsUser() {

        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(userDriver));
        when(tripUserStatusRepository.save(tripUserStatusDriver)).thenReturn(tripUserStatusDriver);
        when(dtoMapper.objectToDtoTrip(tripUserStatusDriver)).thenReturn(tripUserStatusDtoResponseDriver);

        Assert.assertEquals(tripUserStatusDtoResponseDriver, tripUserService.createTripUserStatusAsDriver(trip, "username1"));
    }

    @Test(expected = UsernameNotFoundException.class)
    public void create_tripUserStatus_Should_ThrowException_IfUsernameDoNotReturnUser() {

        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.empty());

        tripUserService.createTripUserStatusAsDriver(trip, "username1");
    }

    @Test
    public void get_userOwnTripsWithDrivers_Should_ReturnListWithResults_WhenUsernameIsProvided() {

        when(tripUserStatusRepository.findAllUserOwnTripsWithDrivers("username1", pageable))
                .thenReturn(tripUserStatusSlice);
        when(dtoMapper.tripUserStatusToDtoList(tripUserStatusList)).thenReturn(tripUserStatusDtoResponsesList);

        Assert.assertEquals(tripUserStatusDtoResponsesList, tripUserService.getUserOwnTripsWithDrivers("username1"));
    }

    @Test
    public void change_UserStatus_Should_ChangeUserStatus_When_CorrectParametersPassed() {

        when(tripRepository.findByModelIdAndIsDeletedFalse(1L)).thenReturn(Optional.ofNullable(trip));
        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(userDriver));
        when(userRepository.findByUsernameAndIsDeletedFalse("username2")).thenReturn(Optional.ofNullable(userPassenger));
        when(tripUserStatusRepository.findAllByTripModelIdAndIsDeletedFalse(1L)).thenReturn(tripUserStatusList);
        when(userStatus.changeUserStatus(userPassenger, userDriver, trip, tripUserStatusList, tripUserService))
                .thenReturn(tripUserStatusPassenger);
        when(tripUserStatusRepository.save(tripUserStatusPassenger)).thenReturn(tripUserStatusPassenger);

        tripUserService.changeUserStatus(1L, "username2", "username1", userStatus);

        verify(tripRepository, times(1)).findByModelIdAndIsDeletedFalse(1L);
        verify(userRepository, times(1)).findByUsernameAndIsDeletedFalse("username1");
        verify(userRepository, times(1)).findByUsernameAndIsDeletedFalse("username2");
        verify(tripUserStatusRepository, times(1)).findAllByTripModelIdAndIsDeletedFalse(1L);
        verify(userStatus, times(1))
                .changeUserStatus(userPassenger, userDriver, trip, tripUserStatusList, tripUserService);
        verify(tripUserStatusRepository, times(1)).save(tripUserStatusPassenger);
        verifyNoMoreInteractions(tripRepository, userRepository, tripUserStatusRepository, userStatus);

    }

    @Test(expected = UsernameNotFoundException.class)
    public void change_UserStatus_Should_ThrowException_IfLoggedUserUsernameDoNotReturnUser() {

        when(tripRepository.findByModelIdAndIsDeletedFalse(1L)).thenReturn(Optional.ofNullable(trip));
        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.empty());

        tripUserService.changeUserStatus(1L, "username2", "username1", userStatus);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void change_UserStatus_Should_ThrowException_IfPassengerUsernameDoNotReturnUser() {

        when(tripRepository.findByModelIdAndIsDeletedFalse(1L)).thenReturn(Optional.ofNullable(trip));
        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(userDriver));
        when(userRepository.findByUsernameAndIsDeletedFalse("username2")).thenReturn(Optional.empty());

        tripUserService.changeUserStatus(1L, "username2", "username1", userStatus);
    }

    @Test(expected = IllegalArgumentException.class)
    public void change_UserStatus_Should_ThrowException_IfTripModelIdDoNotReturnTrip() {

        when(tripRepository.findByModelIdAndIsDeletedFalse(1L)).thenReturn(Optional.empty());

        tripUserService.changeUserStatus(1L, "username2", "username1", userStatus);
    }

    @Test
    public void adjust_AvailablePlacesAndTripStatusWhenPassengerIsAccepted_ShouldSucceed_When_TripIsPresent() {

        when(tripRepository.save(trip)).thenReturn(trip);

        tripUserService.adjustAvailablePlacesAndTripStatusWhenPassengerIsAccepted(trip);

        verify(tripRepository, times(1)).save(trip);
        verifyNoMoreInteractions(tripRepository);
    }

    @Test
    public void get_CurrentTripUserStatusForAllUsersInATrip_Should_ReturnListWithResults_WhenModelIdReturnsTrip() {

        when(tripRepository.findByModelIdAndIsDeletedFalse(1L)).thenReturn(Optional.ofNullable(trip));
        when(tripUserStatusRepository.findCurrentTripUserStatusForAllUsersByTripAndIsDeletedFalse(trip))
                .thenReturn(tripUserStatusList);
        when(dtoMapper.tripUserStatusToDtoList(tripUserStatusList)).thenReturn(tripUserStatusDtoResponsesList);

        Assert.assertEquals(
                tripUserStatusDtoResponsesList, tripUserService.getCurrentTripUserStatusForAllUsersInATrip(1L));
    }

    @Test(expected = IllegalArgumentException.class)
    public void get_CurrentTripUserStatusForAllUsersInATrip_Should_ThrowException_IfTripModelIdDoNotReturnTrip() {

        when(tripRepository.findByModelIdAndIsDeletedFalse(1L)).thenReturn(Optional.empty());

        tripUserService.getCurrentTripUserStatusForAllUsersInATrip(1L);
    }

    @Test
    public void get_AllTripsWithDrivers_Should_ReturnListWithResults_WhenNoParametersAreProvided() {

        when(tripUserStatusRepository.findAllTripsWithDriversByPassedParameters(null, null, null,
                null, null, null, null, null, null,
                null, pageable))
                .thenReturn(tripUserStatusSlice);
        when(dtoMapper.tripUserStatusToDtoList(tripUserStatusList)).thenReturn(tripUserStatusDtoResponsesList);

        Assert.assertEquals(tripUserStatusDtoResponsesList, tripUserService.getAllTripsWithDrivers(0,
                10, null, null, null, null, null,
                null, null, null, null,null));
    }

    @Test
    public void get_AllTripsWithDrivers_Should_ReturnListWithResults_WhenAllParametersAreProvided() {

        when(tripUserStatusRepository.findAllTripsWithDriversByPassedParameters(TripStatus.AVAILABLE, "null",
                "null",earliestDepartureTime , latestDepartureTime, 3, true, true,
                true, true, pageable))
                .thenReturn(tripUserStatusSlice);
        when(dtoMapper.tripUserStatusToDtoList(tripUserStatusList)).thenReturn(tripUserStatusDtoResponsesList);

        Assert.assertEquals(tripUserStatusDtoResponsesList, tripUserService.getAllTripsWithDrivers(0,
                10, TripStatus.AVAILABLE, "null", "null", "2020-11-30 12:00",
                "2020-12-30 12:00",
                3, true, true, true,true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void get_AllTripsWithDrivers_Should_ThrowException_WhenEarliestDepartureTimeIsNotValidFormat() {

        tripUserService.getAllTripsWithDrivers(0,
                10, TripStatus.AVAILABLE, "null", "null", "",
                "2020-12-30 12:00",
                3, true, true, true,true);
    }
}

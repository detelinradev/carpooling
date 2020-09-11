package com.telerik.carpooling.ServiceTests;

import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.enums.UserRole;
import com.telerik.carpooling.enums.UserStatus;
import com.telerik.carpooling.model.Rating;
import com.telerik.carpooling.model.Trip;
import com.telerik.carpooling.model.TripUserStatus;
import com.telerik.carpooling.model.User;
import com.telerik.carpooling.repository.RatingRepository;
import com.telerik.carpooling.repository.TripRepository;
import com.telerik.carpooling.repository.TripUserStatusRepository;
import com.telerik.carpooling.repository.UserRepository;
import com.telerik.carpooling.service.RatingServiceImpl;
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
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class RatingServiceTests {

    @Mock
    RatingRepository ratingRepository;

    @Mock
    TripRepository tripRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    TripUserStatusRepository tripUserStatusRepository;

    private Rating rating;
    private Trip trip;
    private TripUserStatus tripUserStatusDriver;
    private TripUserStatus tripUserStatusPassenger;
    private User user1;
    private User user2;
    private User user3;
    private List<TripUserStatus> tripUserStatusList;

    @Spy
    @InjectMocks
    RatingServiceImpl ratingService;

    @Before
    public void SetUp() {

        user1 = new User("username1", "lastName", "username1",
                "email@gmail.com", UserRole.USER, "password", "phone", 3.5,
                4.0);
        user1.setModelId(1L);
        user2 = new User("username2", "lastName", "username2",
                "email@gmail.com", UserRole.USER, "password", "phone", 3.5,
                4.0);
        user2.setModelId(2L);
        user3 = new User("username3", "lastName", "username3",
                "email@gmail.com", UserRole.USER, "password", "phone", 3.5,
                4.0);
        user2.setModelId(3L);
        rating = new Rating(user1, user2, 3, false);
        trip = new Trip("message", LocalDateTime.MAX,
                "origin", "destination", 3, 5, 4,
                true, true, true, true, TripStatus.AVAILABLE);
        trip.setModelId(1L);
        tripUserStatusDriver = new TripUserStatus(user1, trip, UserStatus.DRIVER);
        tripUserStatusPassenger = new TripUserStatus(user2, trip, UserStatus.ACCEPTED);
        tripUserStatusList = new ArrayList<>();
        tripUserStatusList.add(tripUserStatusDriver);
        tripUserStatusList.add(tripUserStatusPassenger);
    }

    @Test
    public void create_Rating_Should_CreateRating_When_AllParametersAreValidAndUsersBelongToTrip(){

        when(tripRepository.findByModelIdAndIsDeletedFalse(1L)).thenReturn(Optional.ofNullable(trip));
        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(user1));
        when(userRepository.findByUsernameAndIsDeletedFalse("username2")).thenReturn(Optional.ofNullable(user2));
        when(tripUserStatusRepository.findCurrentTripUserStatusForAllUsersByTripAndIsDeletedFalse(trip))
                .thenReturn(tripUserStatusList);
        when(ratingRepository.save(rating)).thenReturn(rating);

        ratingService.createRating(1L,"username1","username2",3);

        verify(tripRepository,times(1))
                .findByModelIdAndIsDeletedFalse(1L);
        verify(userRepository,times(1))
                .findByUsernameAndIsDeletedFalse("username1");
        verify(userRepository,times(1))
                .findByUsernameAndIsDeletedFalse("username2");
        verify(tripUserStatusRepository,times(1))
                .findCurrentTripUserStatusForAllUsersByTripAndIsDeletedFalse(trip);
        verify(ratingRepository,times(1)).save(rating);

        verifyNoMoreInteractions(userRepository,tripUserStatusRepository,tripRepository,ratingRepository);
    }

    @Test (expected = IllegalArgumentException.class)
    public void create_Rating_Should_ThrowException_IfTripModelIdIsNotValid() {

        when(tripRepository.findByModelIdAndIsDeletedFalse(1L))
                .thenReturn(Optional.empty());

        ratingService.createRating(1L,"username1","username2",3);
    }

    @Test (expected = UsernameNotFoundException.class)
    public void create_Rating_Should_ThrowException_IfLoggedUserUsernameIsNotValid() {

        when(tripRepository.findByModelIdAndIsDeletedFalse(1L)).thenReturn(Optional.ofNullable(trip));
        when(userRepository.findByUsernameAndIsDeletedFalse("username1"))
                .thenReturn(Optional.empty());

        ratingService.createRating(1L,"username1","username2",3);
    }

    @Test (expected = UsernameNotFoundException.class)
    public void create_Rating_Should_ThrowException_IfRatedUserUsernameIsNotValid() {

        when(tripRepository.findByModelIdAndIsDeletedFalse(1L)).thenReturn(Optional.ofNullable(trip));
        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(user1));
        when(userRepository.findByUsernameAndIsDeletedFalse("username2"))
                .thenReturn(Optional.empty());

        ratingService.createRating(1L,"username1","username2",3);
    }

    @Test (expected = IllegalArgumentException.class)
    public void create_Rating_Should_ThrowException_IfRatedUserDoNotBelongToTheTrip() {

        tripUserStatusPassenger.setUser(user3);

        when(tripRepository.findByModelIdAndIsDeletedFalse(1L)).thenReturn(Optional.ofNullable(trip));
        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(user1));
        when(userRepository.findByUsernameAndIsDeletedFalse("username2")).thenReturn(Optional.ofNullable(user2));
        when(tripUserStatusRepository.findCurrentTripUserStatusForAllUsersByTripAndIsDeletedFalse(trip))
                .thenReturn(tripUserStatusList);

        ratingService.createRating(1L,"username1","username2",3);
    }

    @Test (expected = IllegalArgumentException.class)
    public void create_Rating_Should_ThrowException_IfLoggedUserDoNotBelongToTheTrip() {

        tripUserStatusDriver.setUser(user3);

        when(tripRepository.findByModelIdAndIsDeletedFalse(1L)).thenReturn(Optional.ofNullable(trip));
        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(user1));
        when(userRepository.findByUsernameAndIsDeletedFalse("username2")).thenReturn(Optional.ofNullable(user2));
        when(tripUserStatusRepository.findCurrentTripUserStatusForAllUsersByTripAndIsDeletedFalse(trip))
                .thenReturn(tripUserStatusList);

        ratingService.createRating(1L,"username1","username2",3);
    }

    @Test (expected = IllegalArgumentException.class)
    public void create_Rating_Should_ThrowException_IfNeitherOfBothUsersIsDriver() {

        tripUserStatusDriver.setUserStatus(UserStatus.ACCEPTED);

        when(tripRepository.findByModelIdAndIsDeletedFalse(1L)).thenReturn(Optional.ofNullable(trip));
        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(user1));
        when(userRepository.findByUsernameAndIsDeletedFalse("username2")).thenReturn(Optional.ofNullable(user2));
        when(tripUserStatusRepository.findCurrentTripUserStatusForAllUsersByTripAndIsDeletedFalse(trip))
                .thenReturn(tripUserStatusList);

        ratingService.createRating(1L,"username1","username2",3);
    }
}

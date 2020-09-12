package com.telerik.carpooling.ServiceTests;

import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.enums.UserRole;
import com.telerik.carpooling.enums.UserStatus;
import com.telerik.carpooling.model.Feedback;
import com.telerik.carpooling.model.Trip;
import com.telerik.carpooling.model.TripUserStatus;
import com.telerik.carpooling.model.User;
import com.telerik.carpooling.model.dto.FeedbackDtoResponse;
import com.telerik.carpooling.model.dto.UserDtoResponse;
import com.telerik.carpooling.model.dto.dto.mapper.DtoMapper;
import com.telerik.carpooling.repository.FeedbackRepository;
import com.telerik.carpooling.repository.TripRepository;
import com.telerik.carpooling.repository.TripUserStatusRepository;
import com.telerik.carpooling.repository.UserRepository;
import com.telerik.carpooling.service.FeedbackServiceImpl;
import com.telerik.carpooling.service.service.contract.RatingService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class FeedbackServiceTests {

    @Mock
    FeedbackRepository feedbackRepository;

    @Mock
    TripRepository tripRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    TripUserStatusRepository tripUserStatusRepository;

    @Mock
    RatingService ratingService;

    @Mock
    DtoMapper dtoMapper;

    private Feedback feedback;
    private Trip trip;
    private TripUserStatus tripUserStatusDriver;
    private TripUserStatus tripUserStatusPassenger;
    private User user1;
    private User user2;
    private User user3;
    private List<TripUserStatus> tripUserStatusList;
    private Set<FeedbackDtoResponse> feedbackDtoResponseSet;
    private Set<Feedback> feedbackSet;

    @Spy
    @InjectMocks
    FeedbackServiceImpl feedbackService;

    @Before
    public void SetUp() {

        user1 = new User("username1", "lastName", "username1",
                "email@gmail.com", UserRole.USER, "password", "phone", 3.5,
                4.0, 3, 4.0, 3, 4.0);
        user1.setModelId(1L);
        user2 = new User("username2", "lastName", "username2",
                "email@gmail.com", UserRole.USER, "password", "phone", 3.5,
                4.0,
                3, 4.0, 3, 4.0);
        user2.setModelId(2L);
        user3 = new User("username3", "lastName", "username3",
                "email@gmail.com", UserRole.USER, "password", "phone", 3.5,
                4.0, 3, 4.0, 3, 4.0);
        user3.setModelId(3L);
        UserDtoResponse userDtoResponse = new UserDtoResponse(1L, "username1", "firstName", "lastName",
                "email@gmail.com", UserRole.USER, "phone", 3.5,
                4.0);
        userDtoResponse.setModelId(1L);
        feedback = new Feedback(user1, user2, "feedback", false);
        feedbackSet = new HashSet<>();
        feedbackSet.add(feedback);
        FeedbackDtoResponse feedbackDtoResponse = new FeedbackDtoResponse(1L, userDtoResponse, "feedback");
        feedbackDtoResponseSet= new HashSet<>();
        feedbackDtoResponseSet.add(feedbackDtoResponse);
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
    public void leave_Feedback_Should_CreateFeedback_When_AllParametersAreValidAndUsersBelongToTrip() {

        when(tripRepository.findByModelIdAndIsDeletedFalse(1L)).thenReturn(Optional.ofNullable(trip));
        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(user1));
        when(userRepository.findByUsernameAndIsDeletedFalse("username2")).thenReturn(Optional.ofNullable(user2));
        when(tripUserStatusRepository.findCurrentTripUserStatusForAllUsersByTripAndIsDeletedFalse(trip))
                .thenReturn(tripUserStatusList);
        when(ratingService.doLoggedUserAndInteractedUserBothBelongToTripAndOneOfThemIsDriver(user1,user2,tripUserStatusList))
                .thenReturn(true);
        when(feedbackRepository.save(feedback)).thenReturn(feedback);

        feedbackService.leaveFeedback(1L, "username1", "username2", "feedback");

        verify(tripRepository, times(1)).findByModelIdAndIsDeletedFalse(1L);
        verify(userRepository, times(1)).findByUsernameAndIsDeletedFalse("username1");
        verify(userRepository, times(1)).findByUsernameAndIsDeletedFalse("username2");
        verify(tripUserStatusRepository, times(1)).findCurrentTripUserStatusForAllUsersByTripAndIsDeletedFalse(trip);
        verify(feedbackRepository, times(1)).save(feedback);

        verifyNoMoreInteractions(userRepository, tripUserStatusRepository, tripRepository, feedbackRepository);
    }

    @Test(expected = IllegalArgumentException.class)
    public void leave_Feedback_Should_ThrowException_IfTripModelIdIsNotValid() {

        when(tripRepository.findByModelIdAndIsDeletedFalse(1L))
                .thenReturn(Optional.empty());

        feedbackService.leaveFeedback(1L, "username1", "username2", "feedback");
    }

    @Test(expected = IllegalArgumentException.class)
    public void leave_Feedback_Should_ThrowException_IfLoggedUserUsernameIsNotValid() {

        when(tripRepository.findByModelIdAndIsDeletedFalse(1L)).thenReturn(Optional.ofNullable(trip));
        when(userRepository.findByUsernameAndIsDeletedFalse("username1"))
                .thenReturn(Optional.empty());

        feedbackService.leaveFeedback(1L, "username1", "username2", "feedback");
    }

    @Test(expected = IllegalArgumentException.class)
    public void leave_Feedback_Should_ThrowException_IfUserUsernameIsNotValid() {

        when(tripRepository.findByModelIdAndIsDeletedFalse(1L)).thenReturn(Optional.ofNullable(trip));
        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(user1));
        when(userRepository.findByUsernameAndIsDeletedFalse("username2"))
                .thenReturn(Optional.empty());

        feedbackService.leaveFeedback(1L, "username1", "username2", "feedback");
    }

    @Test(expected = IllegalArgumentException.class)
    public void leave_Feedback_Should_ThrowException_IfUserDoNotBelongToTheTrip() {

        tripUserStatusPassenger.setUser(user3);

        when(tripRepository.findByModelIdAndIsDeletedFalse(1L)).thenReturn(Optional.ofNullable(trip));
        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(user1));
        when(userRepository.findByUsernameAndIsDeletedFalse("username2")).thenReturn(Optional.ofNullable(user2));
        when(tripUserStatusRepository.findCurrentTripUserStatusForAllUsersByTripAndIsDeletedFalse(trip))
                .thenReturn(tripUserStatusList);
        when(ratingService.doLoggedUserAndInteractedUserBothBelongToTripAndOneOfThemIsDriver(user1,user2,tripUserStatusList))
                .thenReturn(false);

        feedbackService.leaveFeedback(1L, "username1", "username2", "feedback");
    }

    @Test(expected = IllegalArgumentException.class)
    public void leave_Feedback_Should_ThrowException_IfLoggedUserDoNotBelongToTheTrip() {

        tripUserStatusDriver.setUser(user3);

        when(tripRepository.findByModelIdAndIsDeletedFalse(1L)).thenReturn(Optional.ofNullable(trip));
        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(user1));
        when(userRepository.findByUsernameAndIsDeletedFalse("username2")).thenReturn(Optional.ofNullable(user2));
        when(tripUserStatusRepository.findCurrentTripUserStatusForAllUsersByTripAndIsDeletedFalse(trip))
                .thenReturn(tripUserStatusList);
        when(ratingService.doLoggedUserAndInteractedUserBothBelongToTripAndOneOfThemIsDriver(user1,user2,tripUserStatusList))
                .thenReturn(false);

        feedbackService.leaveFeedback(1L, "username1", "username2", "feedback");
    }

    @Test(expected = IllegalArgumentException.class)
    public void leave_Feedback_Should_ThrowException_IfNeitherOfBothUsersIsDriver() {

        tripUserStatusDriver.setUserStatus(UserStatus.ACCEPTED);

        when(tripRepository.findByModelIdAndIsDeletedFalse(1L)).thenReturn(Optional.ofNullable(trip));
        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(user1));
        when(userRepository.findByUsernameAndIsDeletedFalse("username2")).thenReturn(Optional.ofNullable(user2));
        when(tripUserStatusRepository.findCurrentTripUserStatusForAllUsersByTripAndIsDeletedFalse(trip))
                .thenReturn(tripUserStatusList);
        when(ratingService.doLoggedUserAndInteractedUserBothBelongToTripAndOneOfThemIsDriver(user1,user2,tripUserStatusList))
                .thenReturn(false);

        feedbackService.leaveFeedback(1L, "username1", "username2", "feedback");
    }

    @Test
    public void get_Feedback_Should_ReturnSetWithResults_WhenUsernameIsValid() {

        when(userRepository.findByUsernameAndIsDeletedFalse("username1"))
                .thenReturn(Optional.ofNullable(user1));
        when(feedbackRepository.getAllByUserAndIsDeletedFalse(user1)).thenReturn(feedbackSet);
        when(dtoMapper.feedbackToFeedbackDtoResponses(feedbackSet)).thenReturn(feedbackDtoResponseSet);

        Assert.assertEquals(feedbackDtoResponseSet, feedbackService.getFeedback("username1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void get_Feedback_Should_ThrowException_IfUsernameIsNotValid() {

        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.empty());

        feedbackService.getFeedback("username1");
    }
}

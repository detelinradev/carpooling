package com.telerik.carpooling.ServiceTests;

import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.enums.UserRole;
import com.telerik.carpooling.enums.UserStatus;
import com.telerik.carpooling.model.Comment;
import com.telerik.carpooling.model.Trip;
import com.telerik.carpooling.model.TripUserStatus;
import com.telerik.carpooling.model.User;
import com.telerik.carpooling.model.dto.CommentDtoResponse;
import com.telerik.carpooling.model.dto.UserDtoResponse;
import com.telerik.carpooling.model.dto.dto.mapper.DtoMapper;
import com.telerik.carpooling.repository.*;
import com.telerik.carpooling.service.CommentServiceImpl;
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

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CommentServiceTests {

    @Mock
    FeedbackRepository feedbackRepository;

    @Mock
    TripRepository tripRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    TripUserStatusRepository tripUserStatusRepository;

    @Mock
    CommentRepository commentRepository;

    @Mock
    DtoMapper dtoMapper;

    private Comment comment;
    private CommentDtoResponse commentDtoResponse;
    private Trip trip;
    private TripUserStatus tripUserStatusDriver;
    private TripUserStatus tripUserStatusPassenger;
    private User user1;
    private User user2;
    private User user3;
    private UserDtoResponse userDtoResponse;
    private List<TripUserStatus> tripUserStatusList;
    private Set<CommentDtoResponse> commentDtoResponseSet;
    private Set<Comment> commentSet;

    @Spy
    @InjectMocks
    CommentServiceImpl commentService;

    @Before
    public void SetUp() {

        user1 = new User("username1", "firstName", "lastName",
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
        userDtoResponse = new UserDtoResponse(1L, "username1", "firstName", "lastName",
                "email@gmail.com", UserRole.USER, "phone", 3.5,
                4.0);
        userDtoResponse.setModelId(1L);
        trip = new Trip("message", LocalDateTime.MAX,
                "origin", "destination", 3, 5, 4,
                true, true, true, true, TripStatus.AVAILABLE);
        trip.setModelId(1L);
        comment = new Comment(user1, trip, "message");
        commentSet = new HashSet<>();
        commentSet.add(comment);
        commentDtoResponse = new CommentDtoResponse(1L, userDtoResponse,"message");
        commentDtoResponseSet = new HashSet<>();
        commentDtoResponseSet.add(commentDtoResponse);
        tripUserStatusDriver = new TripUserStatus(user1, trip, UserStatus.DRIVER);
        tripUserStatusPassenger = new TripUserStatus(user2, trip, UserStatus.ACCEPTED);
        tripUserStatusList = new ArrayList<>();
        tripUserStatusList.add(tripUserStatusDriver);
        tripUserStatusList.add(tripUserStatusPassenger);
    }

    @Test
    public void create_trip_Should_CreateNewTrip_WhenCarIsPresent()  {

        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(user1));
        when(tripRepository.findByModelIdAndIsDeletedFalse(1L)).thenReturn(Optional.ofNullable(trip));
        when(commentRepository.save(comment)).thenReturn(comment);
        when(dtoMapper.objectToDto(comment)).thenReturn(commentDtoResponse);

        Assert.assertEquals(commentDtoResponse,commentService.createComment(1L,"username1", "message"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void create_trip_Should_ThrowException_IfTripModelIdIsNotValid() {

        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(user1));
        when(tripRepository.findByModelIdAndIsDeletedFalse(1L))
                .thenReturn(Optional.empty());

        commentService.createComment(1L, "username1", "message");
    }

    @Test(expected = IllegalArgumentException.class)
    public void create_trip_Should_ThrowException_IfUserUsernameIsNotValid() {

        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.empty());

        commentService.createComment(1L, "username1", "message");
    }

    @Test
    public void get_Comments_Should_ReturnSetWithResults_WhenTripIdIsValid() {

        when(tripRepository.findByModelIdAndIsDeletedFalse(1L)).thenReturn(Optional.ofNullable(trip));
        when(commentRepository.getAllByTripAndIsDeletedFalse(trip)).thenReturn(commentSet);
        when(dtoMapper.commentsToCommentsDtoResponses(commentSet)).thenReturn(commentDtoResponseSet);

        Assert.assertEquals(commentDtoResponseSet, commentService.getComments(1L));
    }

    @Test(expected = IllegalArgumentException.class)
    public void get_Comments_Should_ThrowException_IfTripModelIdIsNotValid() {

        when(tripRepository.findByModelIdAndIsDeletedFalse(1L)).thenReturn(Optional.empty());

        commentService.createComment(1L, "username1", "message");
    }

}

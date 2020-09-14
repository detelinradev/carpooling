package com.telerik.carpooling.serviceTests;

import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.enums.UserRole;
import com.telerik.carpooling.model.Comment;
import com.telerik.carpooling.model.Trip;
import com.telerik.carpooling.model.User;
import com.telerik.carpooling.model.dto.CommentDtoEdit;
import com.telerik.carpooling.model.dto.CommentDtoResponse;
import com.telerik.carpooling.model.dto.UserDtoResponse;
import com.telerik.carpooling.model.dto.dto.mapper.DtoMapper;
import com.telerik.carpooling.repository.CommentRepository;
import com.telerik.carpooling.repository.TripRepository;
import com.telerik.carpooling.repository.UserRepository;
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
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CommentServiceTests {

    @Mock
    TripRepository tripRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    CommentRepository commentRepository;

    @Mock
    DtoMapper dtoMapper;

    private Comment comment;
    private CommentDtoResponse commentDtoResponse;
    private Trip trip;
    private User user1;
    private User user2;
    private CommentDtoEdit commentDtoEdit;
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
        UserDtoResponse userDtoResponse = new UserDtoResponse(1L, "username1", "firstName", "lastName",
                "email@gmail.com", UserRole.USER, "phone", 3.5,
                4.0);
        userDtoResponse.setModelId(1L);
        trip = new Trip("message", LocalDateTime.MAX,
                "origin", "destination", 3, 5, 4,
                true, true, true, true, TripStatus.AVAILABLE);
        trip.setModelId(1L);
        comment = new Comment(user1, trip, "message");
        commentDtoEdit = new CommentDtoEdit(1L,"message");
        commentSet = new HashSet<>();
        commentSet.add(comment);
        commentDtoResponse = new CommentDtoResponse(1L, userDtoResponse,"message");
        commentDtoResponseSet = new HashSet<>();
        commentDtoResponseSet.add(commentDtoResponse);
    }

    @Test
    public void create_Comment_Should_CreateNewComment_WhenTripIdAndLoggedUserUsernameIsValid()  {

        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(user1));
        when(tripRepository.findByModelIdAndIsDeletedFalse(1L)).thenReturn(Optional.ofNullable(trip));
        when(commentRepository.save(comment)).thenReturn(comment);
        when(dtoMapper.objectToDto(comment)).thenReturn(commentDtoResponse);

        Assert.assertEquals(commentDtoResponse,commentService.createComment(1L,"username1", "message"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void create_Comment_Should_ThrowException_IfTripModelIdIsNotValid() {

        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(user1));
        when(tripRepository.findByModelIdAndIsDeletedFalse(1L))
                .thenReturn(Optional.empty());

        commentService.createComment(1L, "username1", "message");
    }

    @Test(expected = IllegalArgumentException.class)
    public void create_Comment_Should_ThrowException_IfUserUsernameIsNotValid() {

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

    @Test
    public void delete_Comment_Should_DeleteComment_When_CommentIdIsValidAndLoggedUserSameUser() {

        when(commentRepository.findById(1L)).thenReturn(Optional.ofNullable(comment));
        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(user1));
        when(commentRepository.save(comment)).thenReturn(comment);

        commentService.deleteComment(1L, "username1");

        verify(commentRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findByUsernameAndIsDeletedFalse("username1");
        verify(commentRepository, times(1)).save(comment);

        verifyNoMoreInteractions(userRepository, commentRepository);
    }

    @Test
    public void delete_Comment_Should_DeleteComment_When_CommentIdIsValidAndLoggedUserAdmin() {

        user2.setRole(UserRole.ADMIN);
        when(commentRepository.findById(1L)).thenReturn(Optional.ofNullable(comment));
        when(userRepository.findByUsernameAndIsDeletedFalse("username2")).thenReturn(Optional.ofNullable(user2));
        when(commentRepository.save(comment)).thenReturn(comment);

        commentService.deleteComment(1L, "username2");

        verify(commentRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findByUsernameAndIsDeletedFalse("username2");
        verify(commentRepository, times(1)).save(comment);

        verifyNoMoreInteractions(userRepository, commentRepository);
    }

    @Test(expected = IllegalArgumentException.class)
    public void delete_Comment_Should_ThrowException_IfCommentModelIdIsNotValid() {

        when(commentRepository.findById(1L)).thenReturn(Optional.empty());

        commentService.deleteComment(1L, "username1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void delete_Comment_Should_ThrowException_IfLoggedUserUsernameIsNotValid() {

        when(commentRepository.findById(1L)).thenReturn(Optional.ofNullable(comment));
        when(userRepository.findByUsernameAndIsDeletedFalse("username2")).thenReturn(Optional.empty());

        commentService.deleteComment(1L, "username1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void delete_Comment_Should_ThrowException_IfLoggedUserIsNotAuthorOrAdmin() {

        when(commentRepository.findById(1L)).thenReturn(Optional.ofNullable(comment));
        when(userRepository.findByUsernameAndIsDeletedFalse("username2")).thenReturn(Optional.ofNullable(user2));

        commentService.deleteComment(1L, "username2");
    }

    @Test
    public void update_Comment_Should_UpdateComment_WhenLoggedUserSameUserAndLoggedUserUsernameIsValid()  {

        when(dtoMapper.dtoToObject(commentDtoEdit)).thenReturn(comment);
        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(user1));
        when(commentRepository.save(comment)).thenReturn(comment);
        when(dtoMapper.objectToDto(comment)).thenReturn(commentDtoResponse);

        Assert.assertEquals(commentDtoResponse,commentService.updateComment(commentDtoEdit, "username1"));
    }

    @Test
    public void update_Comment_Should_UpdateComment_WhenLoggedUserIsAdminAndLoggedUserUsernameIsValid()  {

        user2.setRole(UserRole.ADMIN);
        when(dtoMapper.dtoToObject(commentDtoEdit)).thenReturn(comment);
        when(userRepository.findByUsernameAndIsDeletedFalse("username2")).thenReturn(Optional.ofNullable(user2));
        when(commentRepository.save(comment)).thenReturn(comment);
        when(dtoMapper.objectToDto(comment)).thenReturn(commentDtoResponse);

        Assert.assertEquals(commentDtoResponse,commentService.updateComment(commentDtoEdit, "username2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void update_Comment_Should_ThrowException_IfLoggedUserIsNotAuthorOrAdmin() {

        when(dtoMapper.dtoToObject(commentDtoEdit)).thenReturn(comment);
        when(userRepository.findByUsernameAndIsDeletedFalse("username2")).thenReturn(Optional.ofNullable(user2));

        commentService.updateComment(commentDtoEdit, "username2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void update_Comment_Should_ThrowException_IfLoggedUserUsernameIsNotValid() {

        when(dtoMapper.dtoToObject(commentDtoEdit)).thenReturn(comment);
        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.empty());

        commentService.updateComment(commentDtoEdit, "username1");
    }
}

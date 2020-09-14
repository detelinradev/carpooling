package com.telerik.carpooling.serviceTests;


import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.enums.UserRole;
import com.telerik.carpooling.enums.UserStatus;
import com.telerik.carpooling.model.Trip;
import com.telerik.carpooling.model.TripUserStatus;
import com.telerik.carpooling.model.User;
import com.telerik.carpooling.model.dto.UserDtoEdit;
import com.telerik.carpooling.model.dto.UserDtoRequest;
import com.telerik.carpooling.model.dto.UserDtoResponse;
import com.telerik.carpooling.model.dto.dto.mapper.DtoMapper;
import com.telerik.carpooling.repository.TripUserStatusRepository;
import com.telerik.carpooling.repository.UserRepository;
import com.telerik.carpooling.service.UserServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UserServiceTests {

    @Mock
    UserRepository userRepository;

    @Mock
    TripUserStatusRepository tripUserStatusRepository;

    @Mock
    BCryptPasswordEncoder bCryptEncoder;

    @Mock
    DtoMapper dtoMapper;

    private TripUserStatus tripUserStatus;
    private User user1;
    private User user2;
    private UserDtoRequest userDtoRequest;
    private UserDtoResponse userDtoResponse;
    private UserDtoEdit userDtoEdit;
    private List<User> userList;
    private List<UserDtoResponse> userDtoResponseList;
    private List<TripUserStatus> tripUserStatusList;
    private Slice<User> userSlice;
    private Pageable pageable;

    @Spy
    @InjectMocks
    UserServiceImpl userService;

    @Before
    public void SetUp() {

        user1 = new User("username1", "lastName", "username1",
                "email@gmail.com", UserRole.USER, "password", "phone", 3.5,
                4.0,3, 4.0, 3, 4.0);
        user1.setModelId(1L);
        user2 = new User("username2", "lastName", "username2",
                "email@gmail.com", UserRole.USER, "password", "phone", 3.5,
                4.0,3, 4.0, 3, 4.0);
        user2.setModelId(2L);
        userDtoRequest = new UserDtoRequest("username1", "lastName", "username1",
                "email@gmail.com", "password", "phone");
        userDtoResponse = new UserDtoResponse(1L, "username1", "lastName", "username1",
                "email@gmail.com", UserRole.USER, "phone", 3.5, 4.0);
        userDtoEdit = new UserDtoEdit (1L,"email@gmail.com", UserRole.USER,"password", "phone");
        Trip trip = new Trip("message", LocalDateTime.MAX,
                "origin", "destination", 3, 5, 4,
                true, true, true, true, TripStatus.AVAILABLE);
        trip.setModelId(1L);
        tripUserStatus = new TripUserStatus(user1, trip, UserStatus.PENDING);
        userList = new ArrayList<>();
        userList.add(user1);
        userDtoResponseList = new ArrayList<>();
        userDtoResponseList.add(userDtoResponse);
        tripUserStatusList = new ArrayList<>();
        tripUserStatusList.add(tripUserStatus);
        userSlice = new SliceImpl<>(userList);
        pageable = PageRequest.of(0, 10, Sort.by("lastName").ascending().and(Sort.by("firstName").ascending()));
    }

    @Test
    public void create_User_Should_CreateNewUser_When_DtoIsValid() {

        when(dtoMapper.dtoToObject(userDtoRequest)).thenReturn(user1);
        when(userRepository.save(user1)).thenReturn(user1);
        when(dtoMapper.objectToDto(user1)).thenReturn(userDtoResponse);
        when(bCryptEncoder.encode(userDtoRequest.getPassword())).thenReturn("password");

        Assert.assertEquals(userDtoResponse, userService.createUser(userDtoRequest));
    }

    @Test (expected = IllegalArgumentException.class)
    public void create_User_Should_ThrowException_IfNoPasswordIsPresent(){

        user1.setPassword(null);

        when(dtoMapper.dtoToObject(userDtoRequest)).thenReturn(user1);

        userService.createUser(userDtoRequest);
    }

    @Test
    public void update_User_Should_UpdateUser_When_UserIsLoggedUserAndPasswordIsPresent() {

        when(userRepository.save(user1)).thenReturn(user1);
        when(userRepository.findByUsernameAndIsDeletedFalse(user1.getUsername()))
                .thenReturn(java.util.Optional.ofNullable(user1));
        when(dtoMapper.dtoToObject(userDtoEdit)).thenReturn(user1);
        when(dtoMapper.objectToDto(user1)).thenReturn(userDtoResponse);
        when(bCryptEncoder.encode(userDtoRequest.getPassword())).thenReturn("password");

        Assert.assertEquals(userDtoResponse, userService.updateUser(userDtoEdit,"username1"));
    }

    @Test
    public void update_User_Should_UpdateUser_When_UserIsLoggedUserAndPasswordIsNotPresent() {

        userDtoEdit.setPassword(null);
        when(userRepository.save(user1)).thenReturn(user1);
        when(userRepository.findByUsernameAndIsDeletedFalse(user1.getUsername()))
                .thenReturn(java.util.Optional.ofNullable(user1));
        when(dtoMapper.dtoToObject(userDtoEdit)).thenReturn(user1);
        when(dtoMapper.objectToDto(user1)).thenReturn(userDtoResponse);

        Assert.assertEquals(userDtoResponse, userService.updateUser(userDtoEdit,"username1"));
    }

    @Test
    public void update_User_Should_UpdateUser_When_UserIsAdminAndPasswordIsPresent() {

        user2.setRole(UserRole.ADMIN);
        when(userRepository.findByUsernameAndIsDeletedFalse(user2.getUsername()))
                .thenReturn(java.util.Optional.ofNullable(user2));
        when(dtoMapper.dtoToObject(userDtoEdit)).thenReturn(user1);
        when(userRepository.save(user1)).thenReturn(user1);
        when(dtoMapper.objectToDto(user1)).thenReturn(userDtoResponse);
        when(bCryptEncoder.encode(userDtoRequest.getPassword())).thenReturn("password");

        Assert.assertEquals(userDtoResponse, userService.updateUser(userDtoEdit,"username2"));
    }

    @Test (expected = IllegalArgumentException.class)
    public void update_User_Should_ThrowException_IfUserIsNotLoggedUserAndNotAdmin(){

        when(userRepository.findByUsernameAndIsDeletedFalse(user2.getUsername()))
                .thenReturn(java.util.Optional.ofNullable(user2));
        when(dtoMapper.dtoToObject(userDtoEdit)).thenReturn(user1);


        userService.updateUser(userDtoEdit,"username2");
    }

    @Test (expected = IllegalArgumentException.class)
    public void update_User_Should_ThrowException_IfUserIsNotFound(){

        when(userRepository.findByUsernameAndIsDeletedFalse(user1.getUsername()))
                .thenReturn(Optional.empty());

        userService.updateUser(userDtoEdit,"username1");
    }

    @Test
    public void get_User_Should_RetrieveUser_When_LoggedUserIsSameUserAndUsernameIsValid() {

        when(userRepository.findByUsernameAndIsDeletedFalse(user1.getUsername()))
                .thenReturn(java.util.Optional.ofNullable(user1));
        when(userRepository.findByUsernameAndIsDeletedFalse(user1.getUsername()))
                .thenReturn(java.util.Optional.ofNullable(user1));
        when(dtoMapper.objectToDto(user1)).thenReturn(userDtoResponse);

        Assert.assertEquals(userDtoResponse, userService.getUser("username1","username1"));
    }

    @Test
    public void get_User_Should_RetrieveUser_When_LoggedUserIsAdminAndUsernameIsValid() {

        user2.setRole(UserRole.ADMIN);
        when(userRepository.findByUsernameAndIsDeletedFalse(user1.getUsername()))
                .thenReturn(java.util.Optional.ofNullable(user1));
        when(userRepository.findByUsernameAndIsDeletedFalse(user2.getUsername()))
                .thenReturn(java.util.Optional.ofNullable(user2));
        when(dtoMapper.objectToDto(user1)).thenReturn(userDtoResponse);

        Assert.assertEquals(userDtoResponse, userService.getUser("username1","username2"));
    }

    @Test (expected = IllegalArgumentException.class)
    public void get_User_Should_ThrowException_IfUsernameIsNotValid(){

        when(userRepository.findByUsernameAndIsDeletedFalse(user1.getUsername()))
                .thenReturn(Optional.empty());

        userService.getUser("username1","username1");
    }

    @Test (expected = IllegalArgumentException.class)
    public void get_User_Should_ThrowException_IfLoggedUserIsNotSameUserAndAdmin(){

        when(userRepository.findByUsernameAndIsDeletedFalse(user1.getUsername()))
                .thenReturn(Optional.ofNullable(user1));
        when(userRepository.findByUsernameAndIsDeletedFalse(user2.getUsername()))
                .thenReturn(java.util.Optional.ofNullable(user2));

        userService.getUser("username1","username2");
    }

    @Test
    public void get_Users_Should_RetrieveListWithUsers_When_AllParametersArePassed() {

        when(userRepository.findUsers("username", "firstName","lastName","email",
                "phone", pageable)).thenReturn(userSlice);
        when(dtoMapper.userToDtoList(userList)).thenReturn(userDtoResponseList);

        Assert.assertEquals(userDtoResponseList,userService.getUsers(0,10,"username", "firstName","lastName","email",
                "phone"));
    }

    @Test
    public void get_Users_Should_RetrieveListWithUsers_When_NoParametersArePassed() {

        when(userRepository.findUsers(null, null,null,null,
                null, pageable)).thenReturn(userSlice);
        when(dtoMapper.userToDtoList(userList)).thenReturn(userDtoResponseList);

        Assert.assertEquals(userDtoResponseList,userService.getUsers(0,10,null,
                null,null,null, null));
    }

    @Test
    public void delete_User_Should_deleteUser_WhenPassedUsernameIsValid() {

        when(userRepository.findByUsernameAndIsDeletedFalse("username1"))
                .thenReturn(Optional.ofNullable(user1));
        when( tripUserStatusRepository.findAllByUserAndIsDeletedFalse(user1))
                .thenReturn(tripUserStatusList);
        when(tripUserStatusRepository.save(tripUserStatus)).thenReturn(tripUserStatus);
        when(userRepository.save(user1)).thenReturn(user1);

        userService.deleteUser("username1");

        verify(userRepository,times(1))
                .findByUsernameAndIsDeletedFalse("username1");
        verify(tripUserStatusRepository,times(1))
                .findAllByUserAndIsDeletedFalse(user1);
        verify(tripUserStatusRepository,times(1)).save(tripUserStatus);
        verify(userRepository,times(1)).save(user1);

        verifyNoMoreInteractions(userRepository,tripUserStatusRepository);
    }

    @Test (expected = IllegalArgumentException.class)
    public void delete_User_Should_ThrowException_IfPassedUsernameIsNotValid() {

        when(userRepository.findByUsernameAndIsDeletedFalse("username1"))
                .thenReturn(Optional.empty());

        userService.deleteUser("username1");
    }

    @Test
    public void get_TopRatedUsers_Should_RetrieveListWithUsers_When_ParameterIsPassengerIsTrue() {

        when(userRepository.findUsers(null, null, null,
                null, null, null))
                .thenReturn(userSlice);
        when(dtoMapper.userToDtoList(userList)).thenReturn(userDtoResponseList);

        Assert.assertEquals(userDtoResponseList, userService.getTopRatedUsers(true));
    }

    @Test
    public void get_TopRatedUsers_Should_RetrieveListWithUsers_When_ParameterIsPassengerIsFalse() {

        when(userRepository.findUsers(null, null, null,
                null, null, null))
                .thenReturn(userSlice);
        when(dtoMapper.userToDtoList(userList)).thenReturn(userDtoResponseList);

        Assert.assertEquals(userDtoResponseList, userService.getTopRatedUsers(false));
    }
}

package com.telerik.carpooling.ServiceTests;


import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.enums.UserRole;
import com.telerik.carpooling.enums.UserStatus;
import com.telerik.carpooling.model.Trip;
import com.telerik.carpooling.model.TripUserStatus;
import com.telerik.carpooling.model.User;
import com.telerik.carpooling.model.dto.*;
import com.telerik.carpooling.model.dto.dto.mapper.DtoMapper;
import com.telerik.carpooling.repository.TripRepository;
import com.telerik.carpooling.repository.UserRepository;
import com.telerik.carpooling.service.UserServiceImpl;
import com.telerik.carpooling.service.service.contract.TripService;
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
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UserServiceTests {

    @Mock
    TripRepository tripRepository;
    @Mock
    TripService tripService;

    @Mock
    UserRepository userRepository;

    @Mock
    DtoMapper dtoMapper;

    private Trip trip;
    private TripDtoResponse tripDtoResponse;
    private TripDtoEdit tripDtoEdit;
    private TripDtoRequest tripDtoRequest;
    private TripUserStatus tripUserStatus;
    private User user1;
    private User user2;
    private UserDtoRequest userDtoRequest;
    private UserDtoResponse userDtoResponse;
    private UserDtoEdit userDtoEdit;
    private List<TripUserStatus> tripUserStatusList;

    @Spy
    @InjectMocks
    UserServiceImpl userService;

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
        userDtoRequest = new UserDtoRequest("username1", "lastName", "username1",
                "email@gmail.com", "password", "phone");
        userDtoResponse = new UserDtoResponse(1L, "username1", "lastName", "username1",
                "email@gmail.com", UserRole.USER, "phone", 3.5, 4.0);
        userDtoEdit = new UserDtoEdit (1L,"email@gmail.com", UserRole.USER,"password", "phone");
        trip = new Trip("message", LocalDateTime.MAX,
                "origin", "destination", 3, 5, 4,
                true, true, true, true, TripStatus.AVAILABLE);
        trip.setModelId(1L);
        tripDtoResponse = new TripDtoResponse(1L, "message", LocalDateTime.MAX,
                "origin", "destination", 3, TripStatus.AVAILABLE, 4,
                4, true, true, true, true);
        tripDtoRequest = new TripDtoRequest("message", LocalDateTime.MAX,
                "origin", "destination", 3, 5,
                4, true, true, true, true);
        tripDtoEdit = new TripDtoEdit(1L, "message", LocalDateTime.MAX,
                "origin", "destination", 3, 4,
                4, true, true, true, true);
        tripUserStatus = new TripUserStatus(user1, trip, UserStatus.PENDING);
        tripUserStatusList = new ArrayList<>();
        tripUserStatusList.add(tripUserStatus);
    }

    @Test
    public void create_user_Should_CreateNewUser_When_DtoIsValid() {

        when(userRepository.save(user1)).thenReturn(user1);
        when(dtoMapper.dtoToObject(userDtoRequest)).thenReturn(user1);
        when(dtoMapper.objectToDto(user1)).thenReturn(userDtoResponse);

        Assert.assertEquals(userDtoResponse, userService.createUser(userDtoRequest));
    }

    @Test (expected = IllegalArgumentException.class)
    public void create_user_Should_ThrowException_IfNoPasswordIsPresent(){

        user1.setPassword(null);

        when(dtoMapper.dtoToObject(userDtoRequest)).thenReturn(user1);

        userService.createUser(userDtoRequest);
    }

    @Test
    public void update_user_Should_UpdateUser_When_UserIsLoggedUserOrAdminAndPasswordIsPresent() {

        when(userRepository.save(user1)).thenReturn(user1);
        when(userRepository.findByUsernameAndIsDeletedFalse(user1.getUsername())).thenReturn(java.util.Optional.ofNullable(user1));
        when(dtoMapper.dtoToObject(userDtoEdit)).thenReturn(user1);
        when(dtoMapper.objectToDto(user1)).thenReturn(userDtoResponse);

        Assert.assertEquals(userDtoResponse, userService.updateUser(userDtoEdit,"username1"));
    }

    @Test
    public void update_user_Should_UpdateUser_When_UserIsLoggedUserAndPasswordIsNotPresent() {

        userDtoEdit.setPassword(null);
        when(userRepository.save(user1)).thenReturn(user1);
        when(userRepository.findByUsernameAndIsDeletedFalse(user1.getUsername()))
                .thenReturn(java.util.Optional.ofNullable(user1));
        when(dtoMapper.dtoToObject(userDtoEdit)).thenReturn(user1);
        when(dtoMapper.objectToDto(user1)).thenReturn(userDtoResponse);

        Assert.assertEquals(userDtoResponse, userService.updateUser(userDtoEdit,"username1"));
    }

    @Test
    public void update_user_Should_UpdateUser_When_UserIsAdminAndPasswordIsPresent() {

        user2.setRole(UserRole.ADMIN);
        when(userRepository.findByUsernameAndIsDeletedFalse(user2.getUsername()))
                .thenReturn(java.util.Optional.ofNullable(user2));
        when(dtoMapper.dtoToObject(userDtoEdit)).thenReturn(user1);
        when(userRepository.save(user1)).thenReturn(user1);
        when(dtoMapper.objectToDto(user1)).thenReturn(userDtoResponse);

        Assert.assertEquals(userDtoResponse, userService.updateUser(userDtoEdit,"username2"));
    }

    @Test (expected = IllegalArgumentException.class)
    public void update_user_Should_ThrowException_IfUserIsNotLoggedUserAndNotAdmin(){

        when(userRepository.findByUsernameAndIsDeletedFalse(user2.getUsername()))
                .thenReturn(java.util.Optional.ofNullable(user2));
        when(dtoMapper.dtoToObject(userDtoEdit)).thenReturn(user1);


        userService.updateUser(userDtoEdit,"username2");
    }

    @Test (expected = UsernameNotFoundException.class)
    public void update_user_Should_ThrowException_IfUserIsNotFound(){

        when(userRepository.findByUsernameAndIsDeletedFalse(user1.getUsername()))
                .thenReturn(Optional.empty());

        userService.updateUser(userDtoEdit,"username1");
    }
}

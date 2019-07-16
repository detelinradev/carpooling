package com.telerik.carpooling.services;



import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.UserDto;

public interface UserService {

    User save(UserDto user);

    User updateCurrentUserPassword(String password, User user);

    User updateCurrentUserEmail(String email,User user);
}

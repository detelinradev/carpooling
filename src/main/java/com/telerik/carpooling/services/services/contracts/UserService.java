package com.telerik.carpooling.services.services.contracts;



import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.UserDtoRequest;

public interface UserService {

    User save(UserDtoRequest user);

    User updateCurrentUserPassword(String password, User user);

    User updateCurrentUserEmail(String email,User user);
}

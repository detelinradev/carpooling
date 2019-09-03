package com.telerik.carpooling.services.services.contracts;



import com.telerik.carpooling.models.dtos.TripDtoResponse;
import com.telerik.carpooling.models.dtos.UserDtoEdit;
import com.telerik.carpooling.models.dtos.UserDtoRequest;
import com.telerik.carpooling.models.dtos.UserDtoResponse;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserService {

    UserDtoResponse save(UserDtoRequest userDtoRequest);

    UserDtoResponse updateUser(UserDtoEdit userDtoEdit, Authentication authentication);

    UserDtoResponse getUser(String username,  Authentication authentication);

    void deleteUser(String username);

    List<UserDtoResponse> getUsers(Integer pageNumber,Integer pageSize,String username,String firstName
            ,String lastName,String email, String phone);

    List<TripDtoResponse> getUserOwnTrips(String loggedUserUsername);

    List<UserDtoResponse> getTopRatedUsers(Boolean isPassenger);

}




package com.telerik.carpooling.service.service.contract;



import com.telerik.carpooling.model.dto.TripDtoResponse;
import com.telerik.carpooling.model.dto.UserDtoEdit;
import com.telerik.carpooling.model.dto.UserDtoRequest;
import com.telerik.carpooling.model.dto.UserDtoResponse;

import java.util.List;

public interface UserService {

    UserDtoResponse save(UserDtoRequest userDtoRequest);

    UserDtoResponse updateUser(UserDtoEdit userDtoEdit, String loggedUserUsername);

    UserDtoResponse getUser(String username,  String loggedUserUsername);

    void deleteUser(String username);

    List<UserDtoResponse> getUsers(Integer pageNumber,Integer pageSize,String username,String firstName
            ,String lastName,String email, String phone);

    List<TripDtoResponse> getUserOwnTrips(String loggedUserUsername);

    List<UserDtoResponse> getTopRatedUsers(Boolean isPassenger);

}




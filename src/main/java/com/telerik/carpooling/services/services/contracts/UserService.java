package com.telerik.carpooling.services.services.contracts;



import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.TripDtoResponse;
import com.telerik.carpooling.models.dtos.UserDtoRequest;
import com.telerik.carpooling.models.dtos.UserDtoResponse;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User save(User user);

    User updateCurrentUserPassword(String password, User user);

    User updateCurrentUserEmail(String email,User user);

    User updateUser(User user);

    User getUser(String username);

    List<User> getUsers(Integer pageNumber,Integer pageSize,String username,String firstName,String lastName,String email,
                       String phone);

    List<TripDtoResponse> getUserOwnTrips(String username);

    User deleteUser(String userId);

    List<User> getTopRatedPassengers(Integer pageNumber, Integer pageSize, String username, String firstName, String lastName, String email, String phone);

    List<User> getTopRatedDrivers(Integer pageNumber, Integer pageSize, String username, String firstName, String lastName, String email, String phone);

    User updateCurrentUserPhone(String phone, User user);

}




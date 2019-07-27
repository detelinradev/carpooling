package com.telerik.carpooling.services.services.contracts;



import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.TripDtoResponse;
import com.telerik.carpooling.models.dtos.UserDtoRequest;
import com.telerik.carpooling.models.dtos.UserDtoResponse;

import java.util.Optional;

public interface UserService {
   // Optional<User> findUser(Long userId);

    UserDtoResponse save(UserDtoRequest user);

    User updateCurrentUserPassword(String password, User user);

    User updateCurrentUserEmail(String email,User user);

    User leaveFeedbackDriver(String tripID, User user, String feedback);

    User leaveFeedbackPassenger(String tripID, User user,String passengerID, String feedback);

    User updateUser(UserDtoResponse userDtoResponse);

    UserDtoResponse getUser(String username);
}

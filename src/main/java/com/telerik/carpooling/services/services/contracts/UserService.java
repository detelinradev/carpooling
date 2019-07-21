package com.telerik.carpooling.services.services.contracts;



import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.TripDtoResponse;
import com.telerik.carpooling.models.dtos.UserDtoRequest;
import com.telerik.carpooling.models.dtos.UserDtoResponse;

public interface UserService {

    User save(UserDtoRequest user);

    User updateCurrentUserPassword(String password, User user);

    User updateCurrentUserEmail(String email,User user);

    UserDtoResponse rateUser(TripDtoResponse tripResponseDto, User user, String userRole,
                             int ratedUserID, String ratedUserRole, int rating);
}

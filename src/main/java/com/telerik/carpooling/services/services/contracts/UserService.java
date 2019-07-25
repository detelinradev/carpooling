package com.telerik.carpooling.services.services.contracts;



import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.TripDtoResponse;
import com.telerik.carpooling.models.dtos.UserDtoRequest;
import com.telerik.carpooling.models.dtos.UserDtoResponse;

public interface UserService {

    User save(UserDtoRequest user);

    User updateCurrentUserPassword(String password, User user);

    User updateCurrentUserEmail(String email,User user);

    User rateDriver(String tripID, User user, int rating);

    User ratePassenger(String tripID, User user,String passengerID, int rating);

    UserDtoResponse leaveFeedback(TripDtoResponse tripDtoResponse, User user,String userRole,
                             int userToGetFeedbackId, String userToGetFeedbackRole, String feedback);
}

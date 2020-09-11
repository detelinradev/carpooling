package com.telerik.carpooling.service.service.contract;



import com.telerik.carpooling.model.dto.TripDtoResponse;
import com.telerik.carpooling.model.dto.UserDtoEdit;
import com.telerik.carpooling.model.dto.UserDtoRequest;
import com.telerik.carpooling.model.dto.UserDtoResponse;

import java.util.List;

public interface UserService {

    /**
     *     Creates <class>User</class> from given DTO object
     * <p>
     *     It is expected the input to be valid data, based on a validity check in the controller with annotation @Valid
     * and restrains upon the creation of the DTO object. However it that is not the case, validation criteria are as well
     * placed in the actual entity class as well, what would fire an exception if invalid data is provided.
     * <p>
     *     Transactional annotation is added to override class based behavior read only = true, with read only = false, as
     * this method is modifying the entity so we expect Hibernate to observe changes in the current Persistence Context
     * and include update at flush-time.
     * <p>
     *     Encoding of the password is done and <class>user</class> password is set with the encoded value
     *
     * @param userDtoRequest DTO that holds required data for creating <class>User</class> object
     * @return instance of the created <class>User</class> mapped as <class>UserDtoResponse</class>
     */
    UserDtoResponse createUser(UserDtoRequest userDtoRequest);

    UserDtoResponse updateUser(UserDtoEdit userDtoEdit, String loggedUserUsername);

    UserDtoResponse getUser(String username,  String loggedUserUsername);

    void deleteUser(String username);

    List<UserDtoResponse> getUsers(Integer pageNumber,Integer pageSize,String username,String firstName
            ,String lastName,String email, String phone);

    List<TripDtoResponse> getUserOwnTrips(String loggedUserUsername);

    List<UserDtoResponse> getTopRatedUsers(Boolean isPassenger);

}




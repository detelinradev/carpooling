package com.telerik.carpooling.service.service.contract;


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
     *     There is check is <class>password</class> field of this <class>User</class> is present, in that case the
     * password is encrypted and saved as well, otherwise IllegalArgumentException exception is thrown as that is
     * not the expected behavior. The field should be kept optional on the entity and DTO level so <class>user</class>
     * is able to update its information sharing same path without including password if do not want to.
     * <p>
     *     Transactional annotation is added to override class based behavior read only = true, with read only = false, as
     * this method is modifying the entity so we expect Hibernate to observe changes in the current Persistence Context
     * and include update at flush-time.
     *
     * @param userDtoRequest DTO that holds required data for creating <class>User</class> object
     * @return instance of the created <class>User</class> mapped as <class>UserDtoResponse</class>
     */
    UserDtoResponse createUser(UserDtoRequest userDtoRequest);

    /**
     *     Updates <class>User</class> from given DTO object.
     * <p>
     *     There is check if the logged <class>user</class> is the same <class>user</class>> or has <class>UserRole</class>
     * ADMIN, if the criteria is met, this <class>User</class> is updated, otherwise IllegalArgumentException exception
     * is thrown as that is not the expected behavior.
     * <p>
     *     There is check is <class>password</class> field of this <class>User</class> is present, in that case the
     * password is encrypted and saved as well. The field is optional as it is not visible in response and therefore
     * can not be prefilled automatically.
     * <p>
     *     It is expected the input to be valid data, based on a validity check in the controller with annotation @Valid
     * and restrains upon the creation of the DTO object. However it that is not the case, validation criteria are as well
     * placed in the actual entity class as well, what would fire an exception if invalid data is provided.
     * <p>
     *     Transactional annotation is added to override class based behavior read only = true, with read only = false, as
     * this method is modifying the entity so we expect Hibernate to observe changes in the current Persistence Context
     * and include update at flush-time.
     *
     * @param userDtoEdit DTO that holds required data for updating <class>User</class> object
     * @param loggedUserName <class>username</class> of the currently logged <class>user</class> extracted from
     *                       the security context thread
     * @return instance of the updated <class>User</class> mapped as <class>UserDtoResponse</class>
     */
    UserDtoResponse updateUser(UserDtoEdit userDtoEdit, String loggedUserName);

    UserDtoResponse getUser(String username,  String loggedUserUsername);

    void deleteUser(String username);

    List<UserDtoResponse> getUsers(Integer pageNumber,Integer pageSize,String username,String firstName
            ,String lastName,String email, String phone);

    List<UserDtoResponse> getTopRatedUsers(Boolean isPassenger);

}




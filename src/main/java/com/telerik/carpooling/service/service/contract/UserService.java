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

    /**
     *     Fetches from the database this <class>user</class> which <class>username</class> is passed as parameter.
     * <p>
     *     Check is made if logged <class>user</class> is authorized to get <class>user</class> data and if the
     * passed <class>username</class> is valid, if not exception is thrown.
     *
     * @param username the <class>username</class> of the searched <class>user</class>
     * @param loggedUserUsername <class>username</class> of the currently logged <class>user</class> extracted from the
     *                           security context thread
     * @return instance of the fetched <class>user</class> mapped as <class>UserDtoResponse</class>
     */
    UserDtoResponse getUser(String username,  String loggedUserUsername);

    void deleteUser(String username);

    /**
     *    Retrieving from data base all <class>User</class> objects matching the passed parameters.
     * <p>
     *     Includes <class>Pageable</class> object which pageNumber has default value of 0, pageSize - default value
     * of 10 and nested <class>Sort</class> object which sorts result first based on <class>lastName</class>
     * field ascending and then sorts based on <class>firstName</class> field ascending.
     * <p>
     *     All parameters are optional and if any or all of them are missing, they are replaced with null and not taken
     * into account when searching the database.
     *
     * @param pageNumber parameter is optional and represents the number of the page to be retrieved,
     *                   part of <class>Pageable</class> object, has default value of 0
     * @param pageSize parameter is optional and represents the size of the page that will be retrieved,
     *                 part of <class>Pageable</class> object, has default value of 10
     * @param username string representing <class>username</class> field of <class>User</class> object,
     *                 it is optional search parameter
     * @param firstName string representing <class>firstName</class> field of <class>User</class> object,
     *                  it is optional search parameter
     * @param lastName string representing <class>lastName</class> field of <class>User</class> object,
     *                 it is optional search parameter
     * @param email string representing <class>email</class> field of <class>User</class> object,
     *              it is optional search parameter
     * @param phone string representing <class>phone</class> field of <class>User</class> object,
     *              it is optional search parameter
     * @return <class>List</class> with instances of the fetched <class>User</class> objects mapped as
     *         <class>UserDtoResponse</class> objects
     */
    List<UserDtoResponse> getUsers(Integer pageNumber,Integer pageSize,String username,String firstName
            ,String lastName,String email, String phone);

    List<UserDtoResponse> getTopRatedUsers(Boolean isPassenger);

}




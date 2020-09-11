package com.telerik.carpooling.repository;

import com.telerik.carpooling.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     *     Fetches from the database this <class>user</class> which <class>username</class> is passed as parameter if
     * available, or else return <class>Optional</class>empty().
     *
     * @param username the <class>username</class> of the searched <class>user</class>
     * @return optional instance of the fetched <class>user</class>
     */
    Optional<User> findByUsernameAndIsDeletedFalse(String username);

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
     * @param page <class>Pageable</class> object which if no value is provided by the user then pageNumber has default
     *             value of 0, pageSize - default value of 10 and nested <class>Sort</class> object which sorts result
     *             first based on <class>lastName</class> field ascending and then sorts based on
     *             <class>firstName</class> field ascending.
     * @return <class>List</class> with instances of the fetched <class>User</class> objects mapped as
     *         <class>UserDtoResponse</class> objects
     */
    @Query("select u from User u " +
            "where " +
            "(:username is null or :username ='' or u.username = :username) and" +
            "(:firstName is null or :firstName ='' or u.firstName = :firstName) and" +
            "(:lastName is null or :lastName ='' or u.lastName = :lastName) and" +
            "(:email is null or :email ='' or u.email = :email) and" +
            "(u.isDeleted = false) and" +
            "(:phone is null or :phone ='' or u.phone = :phone)")
    Slice<User> findUsers(@Param(value = "username") String username,
                          @Param(value = "firstName") String firstName,
                          @Param(value = "lastName") String lastName,
                          @Param(value = "email") String email,
                          @Param(value = "phone") String phone,
                          Pageable page);
}




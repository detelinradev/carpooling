package com.telerik.carpooling.repositories;

import com.telerik.carpooling.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsernameAndIsDeletedFalse(String username);

    @Query("select u from User u " +
            "where " +
            "(:username is null or :username ='' or u.username = :username) and" +
            "(:firstName is null or :firstName ='' or u.firstName = :firstName) and" +
            "(:lastName is null or :lastName ='' or u.lastName = :lastName) and" +
            "(:email is null or :email ='' or u.email = :email) and" +
            "(u.isDeleted = false) and" +
            "(:phone is null or :phone ='' or u.phone = :phone)")
    List<User> findUsers(@Param(value = "username") String username,
                         @Param(value = "firstName") String firstName,
                         @Param(value = "lastName") String lastName,
                         @Param(value = "email") String email,
                         @Param(value = "phone") String phone,
                         Pageable page);
}




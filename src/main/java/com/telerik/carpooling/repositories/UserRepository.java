package com.telerik.carpooling.repositories;

import com.telerik.carpooling.models.Trip;
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

    User findFirstByUsername(String username);

    List<User> findAllByIsDeletedIsFalse();

    List<User> findAllByIsDeletedIsTrue(Pageable pageable);

    Optional<User> findById(Long id);
}

package com.telerik.carpooling.repository;

import com.telerik.carpooling.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car,Long> {

    @Query("select c from Car c where c.owner.username = :username and c.isDeleted = false")
    Optional<Car> findByOwnerAndIsDeletedFalse(@Param(value = "username" )String username);

}

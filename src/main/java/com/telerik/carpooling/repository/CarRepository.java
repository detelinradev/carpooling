package com.telerik.carpooling.repository;

import com.telerik.carpooling.model.Car;
import com.telerik.carpooling.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car,Long> {

    Optional<Car> findByOwnerAndIsDeletedFalse(User user);

}

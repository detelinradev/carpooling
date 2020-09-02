package com.telerik.carpooling.repository;

import com.telerik.carpooling.model.Car;
import com.telerik.carpooling.model.Image;
import com.telerik.carpooling.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image,Long> {

    Optional<Image> findByUserAndIsDeletedFalse(User user);

    Optional<Image> findByCarAndIsDeletedFalse(Car car);

}

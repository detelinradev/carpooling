package com.telerik.carpooling.repositories;

import com.telerik.carpooling.models.Car;
import com.telerik.carpooling.models.Image;
import com.telerik.carpooling.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image,Long> {

    Optional<Image> findByUser(User user);

    Optional<Image> findByCar(Car car);

}

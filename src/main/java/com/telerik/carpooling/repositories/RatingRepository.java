package com.telerik.carpooling.repositories;

import com.telerik.carpooling.models.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating,Long> {

    @Query("SELECT AVG(r.rating) from Rating r join User u " +
            "on u.id = r.ratedUser  where u.id=:userID and r.isDriver = true")
    Optional<Double> findAverageRatingByUserAsDriver(@Param("userID") long userID);

    @Query("SELECT AVG(r.rating) from Rating r join User u " +
            "on u.id = r.ratedUser  where u.id=:userID and r.isDriver = false")
    Optional<Double> findAverageRatingByUserAsPassenger(@Param("userID") long userID);

    Optional<Rating> findById(Long id);
}

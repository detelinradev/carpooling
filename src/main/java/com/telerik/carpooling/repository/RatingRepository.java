package com.telerik.carpooling.repository;

import com.telerik.carpooling.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.OptionalDouble;


public interface RatingRepository extends JpaRepository<Rating,Long> {

    @Query("SELECT AVG(r.rating) from Rating r join User u " +
            "on u.modelId = r.ratedUser.modelId  where u.modelId=:userID and r.isDriver = true and r.isDeleted = false")
    OptionalDouble findAverageRatingByUserAsDriver(@Param("userID") long userID);

    @Query("SELECT AVG(r.rating) from Rating r join User u " +
            "on u.modelId = r.ratedUser.modelId  where u.modelId=:userID and r.isDriver = false and r.isDeleted = false")
    OptionalDouble findAverageRatingByUserAsPassenger(@Param("userID") long userID);

}

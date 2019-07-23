package com.telerik.carpooling.repositories;

import com.telerik.carpooling.models.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating,Integer> {
}

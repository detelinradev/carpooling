package com.telerik.carpooling.repositories;

import com.telerik.carpooling.models.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.Valid;

@Valid
public interface RatingRepository extends JpaRepository<Rating, Integer> {

}

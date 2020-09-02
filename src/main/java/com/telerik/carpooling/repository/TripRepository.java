package com.telerik.carpooling.repository;

import com.telerik.carpooling.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    Optional<Trip> findByModelIdAndIsDeletedFalse(Long modelId);
}

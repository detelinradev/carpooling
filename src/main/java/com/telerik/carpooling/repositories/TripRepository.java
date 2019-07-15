package com.telerik.carpooling.repositories;

import com.telerik.carpooling.models.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TripRepository extends JpaRepository<Trip,Integer> {

}

package com.telerik.carpooling.repositories;

import com.telerik.carpooling.models.TripUserStatus;
import com.telerik.carpooling.models.Trip;
import com.telerik.carpooling.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripUserStatusRepository extends JpaRepository<TripUserStatus, Long> {

    List<TripUserStatus> findAllByTripAndUserAndIsDeletedFalse(Trip trip, User user);

    List<TripUserStatus> findAllByUserAndIsDeletedFalse(User user);

    List<TripUserStatus> findAllByTripAndIsDeletedFalse(Trip trip);
}

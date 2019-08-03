package com.telerik.carpooling.repositories;

import com.telerik.carpooling.models.Trip;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface TripRepository extends JpaRepository<Trip,Long> {

    Optional<Trip> findById(Long id);

    @Query("select t from Trip t " +
            "join User u on t.driver = u.id " +
            "join Car c on c.owner = u.id " +
            "where " +
            "(:tripStatus is null or :tripStatus ='' or t.tripStatus = :tripStatus) and" +
            " u.id = :driver and" +
            "(:origin is null or :origin ='' or t.origin = :origin) and" +
            "(:destination is null or :destination ='' or t.destination = :destination) and" +
            "(:departureTime is null or :departureTime ='' or t.departureTime = :departureTime) and" +
            "(:availablePlaces is null or :availablePlaces ='' or t.availablePlaces = :availablePlaces) and" +
            "(:smoking is null  or c.isSmokingAllowed = :smoking) and" +
            "(:pets is null or c.isPetsAllowed = :pets) and" +
            "(:luggage is null or c.isLuggageAllowed = :luggage)")
    List<Trip> findTripsByPassedParameters(//@Param(value = "page")PageRequest page,
                                           @Param(value = "tripStatus") String status,
                                           @Param(value = "driver") long driverID,
                                           @Param(value = "origin") String origin,
                                           @Param(value = "destination") String destination,
                                           @Param(value = "departureTime") String departureTime,
                                           @Param(value = "availablePlaces") Long availablePlaces,
                                           @Param(value = "smoking") boolean smoking,
                                           @Param(value = "pets") boolean pets,
                                           @Param(value = "luggage") boolean luggage
                                          // Pageable pageable
    );

}

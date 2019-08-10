package com.telerik.carpooling.repositories;

import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.models.Trip;
import com.telerik.carpooling.models.User;
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
            "where " +
            "(:tripStatus is null or t.tripStatus = :tripStatus) and" +
            "(:driver is null or t.driver = :driver) and" +
            "(:origin is null or :origin ='' or t.origin = :origin) and" +
            "(:destination is null or :destination ='' or t.destination = :destination) and" +
            "(:departureTime is null or :departureTime ='' or t.departureTime = :departureTime) and" +
            "(:availablePlaces is null or t.availablePlaces >= :availablePlaces) and" +
            "(:smoking is null or :smoking ='' or t.smokingAllowed = :smoking) and" +
            "(:pets is null or :pets ='' or t.petsAllowed = :pets) and" +
            "(:luggage is null or :luggage ='' or t.luggageAllowed = :luggage)")
    List<Trip> findTripsByPassedParameters(@Param(value = "tripStatus") TripStatus status,
                                           @Param(value = "driver") User driver,
                                           @Param(value = "origin") String origin,
                                           @Param(value = "destination") String destination,
                                           @Param(value = "departureTime") String departureTime,
                                           @Param(value = "availablePlaces") Integer availablePlaces,
                                           @Param(value = "smoking") String smoking,
                                           @Param(value = "pets") String pets,
                                           @Param(value = "luggage") String luggage,
                                           Pageable page
    );
}

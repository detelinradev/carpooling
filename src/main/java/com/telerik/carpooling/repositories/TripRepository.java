package com.telerik.carpooling.repositories;

import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.models.Trip;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

//    @Query("SELECT t from Trip t where t.modelId=:id and t.isDeleted = false")
    Optional<Trip> findByModelIdAndIsDeletedFalse(Long modelId);

    @Query("select t from Trip t " +
//            "join User u on t.driver = u.id " +
//            "join Car c on c.owner = u.id " +
            "where " +
            "(:tripStatus is null or t.tripStatus = :tripStatus) and" +
            "(:origin is null or :origin ='' or t.origin = :origin) and" +
            "(:destination is null or :destination ='' or t.destination = :destination) and" +
            "(:earliestDepartureTime is null  or t.departureTime >= :earliestDepartureTime) and" +
            "(:latestDepartureTime is null  or t.departureTime <= :latestDepartureTime) and" +
            "(:availablePlaces is null or t.availablePlaces >= :availablePlaces) and" +
            "(:smoking is null or t.smokingAllowed = :smoking) and" +
            "(:pets is null or t.petsAllowed = :pets) and" +
            "(t.isDeleted = false) and" +
            "(:luggage is null or t.luggageAllowed = :luggage)"
//            "(:airConditioned is null or :airConditioned ='' or c.airConditioned = :airConditioned)"
            )
    List<Trip> findTripsByPassedParameters(@Param(value = "tripStatus") TripStatus status,
                                           @Param(value = "origin") String origin,
                                           @Param(value = "destination") String destination,
                                           @Param(value = "earliestDepartureTime") LocalDateTime EarliestDepartureTime,
                                           @Param(value = "latestDepartureTime") LocalDateTime latestDepartureTime,
                                           @Param(value = "availablePlaces") Integer availablePlaces,
                                           @Param(value = "smoking") Boolean smoking,
                                           @Param(value = "pets") Boolean pets,
                                           @Param(value = "luggage") Boolean luggage,
//                                           @Param(value = "airConditioned") Boolean airConditioned,
                                           Pageable page
    );
}

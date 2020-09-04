package com.telerik.carpooling.repository;

import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.enums.UserStatus;
import com.telerik.carpooling.model.Trip;
import com.telerik.carpooling.model.TripUserStatus;
import com.telerik.carpooling.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TripUserStatusRepository extends JpaRepository<TripUserStatus, Long> {

    List<TripUserStatus> findAllByTripAndUserAndIsDeletedFalse(Trip trip, User user);

    @Query("from TripUserStatus tus where tus.trip.modelId = :modelId and tus.isDeleted = false"
    )
    List<TripUserStatus> findAllByTripModelIdAndIsDeletedFalse(@Param(value = "modelId") Long modelId);

    @Query("select tus from TripUserStatus tus " +
            "where tus.trip.modelId = :modelId " +
            "and tus.user.username = :username " +
            "and tus.userStatus = 'F' " +
            "and tus.isDeleted = false"
    )
    Optional<TripUserStatus> findOneByTripModelIdAndUserUsernameAsDriver(@Param(value = "modelId") Long modelId,
                                                                         @Param(value= "username") String username);

    List<TripUserStatus> findAllByUserAndIsDeletedFalse(User user);

    @Query("from TripUserStatus " +
            "where trip = :trip " +
            "group by user " +
            "order by modified desc"
    )
    List<TripUserStatus> findAllTripsWithDriversByTripAndIsDeletedFalse(@Param(value = "trip") Trip trip);

    @Query( "from TripUserStatus " +
            "where userStatus = :userStatus " +
            "and trip in (select tusUser.trip from TripUserStatus tusUser where tusUser.user = :user and isDeleted = false)"
            )
    List<TripUserStatus> findAllUserTripsWithItsDrivers (@Param(value="userStatus") UserStatus userStatus,
                                                         @Param(value ="user")User user);


    @Query("select t from TripUserStatus t " +
            "where " +
            "(:tripStatus is null or t.trip.tripStatus = :tripStatus) and" +
            "(:origin is null or :origin ='' or t.trip.origin = :origin) and" +
            "(:destination is null or :destination ='' or t.trip.destination = :destination) and" +
            "(:earliestDepartureTime is null  or t.trip.departureTime >= :earliestDepartureTime) and" +
            "(:latestDepartureTime is null  or t.trip.departureTime <= :latestDepartureTime) and" +
            "(:availablePlaces is null or t.trip.availablePlaces >= :availablePlaces) and" +
            "(:smoking is null or t.trip.smokingAllowed = :smoking) and" +
            "(:pets is null or t.trip.petsAllowed = :pets) and" +
            "(t.isDeleted = false) and" +
            "(t.userStatus = 'F') and" +
            "(:luggage is null or t.trip.luggageAllowed = :luggage) and" +
            "(:airConditioned is null or :airConditioned ='' or t.trip.airConditioned = :airConditioned)"
    )
    List<TripUserStatus> findTripUserStatusesByPassedParameters(@Param(value = "tripStatus") TripStatus status,
                                           @Param(value = "origin") String origin,
                                           @Param(value = "destination") String destination,
                                           @Param(value = "earliestDepartureTime") LocalDateTime EarliestDepartureTime,
                                           @Param(value = "latestDepartureTime") LocalDateTime latestDepartureTime,
                                           @Param(value = "availablePlaces") Integer availablePlaces,
                                           @Param(value = "smoking") Boolean smoking,
                                           @Param(value = "pets") Boolean pets,
                                           @Param(value = "luggage") Boolean luggage,
                                           @Param(value = "airConditioned") Boolean airConditioned,
                                           Pageable page
    );

    /**
     * Finds <class>TripUserStatus</class> by criteria <class>trip</class> <class>modelId</class>,
     * <class>userStatus</class> - <class>driver</class> or <class>userRole</class> - <class>admin</class> and
     * <class>isDeleted</class> is <class>false</class>.
     *
     * @param tripId the <class>modelId</class> of the <class>trip</class>
     * @param loggedUserUsername <class>username</class> of the currently logged <class>user</class> extracted
     *                           from the security context thread
     * @return optional <class>TripUserStatus</class> object
     */
    @Query("select tus from TripUserStatus tus " +
            "where tus.trip.modelId = :modelId " +
            "and tus.user.username = :username " +
            "and tus.userStatus = 'F' or tus.user.role = 'A' " +
            "and tus.isDeleted = false"
    )
    Optional<TripUserStatus> findOneByTripModelIdAndUserAsDriverOrAdmin(@Param(value = "tripId") Long tripId,
                                                                        @Param(value = "loggedUserUsername") String loggedUserUsername);
}
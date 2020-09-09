package com.telerik.carpooling.service.service.contract;

import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.enums.UserStatus;
import com.telerik.carpooling.exception.MyNotFoundException;
import com.telerik.carpooling.model.Trip;
import com.telerik.carpooling.model.dto.TripUserStatusDtoResponse;

import java.util.List;

public interface TripUserStatusService {

    List<TripUserStatusDtoResponse> userOwnTripsWithDrivers(String username);

    List<TripUserStatusDtoResponse> getAllTripUserStatuses(Integer pageEnd, Integer pageStart, TripStatus tripStatus, String origin,
                                                           String destination, String earliestDepartureTime, String latestDepartureTime,
                                                           Integer availablePlaces, Boolean smoking, Boolean pets, Boolean luggage, Boolean airConditioned);

    List<TripUserStatusDtoResponse> getTripUserStatuses(Long tripID) throws MyNotFoundException;

    List<TripUserStatusDtoResponse> getTripUserStatus(Long tripId) throws MyNotFoundException;

    /**
     *     Creates <class>tripUserStatus</class> from given <class>trip</class> and <class>user</class> with predefined
     * <class>UserStatus</class> as DRIVER, user is retrieved from database based on <class>username</class> provided.
     * <p>
     *     Check is made if <class>user</class> with parameter <class>username</class>
     * exist, if the user exist, <class>user</class> is returned, otherwise <class>UsernameNotFoundException</class>
     * is thrown. Validation for this <class>trip</class> passed as parameter to the method is not required as this is
     * return from method CreateTrip in TripService, where appropriate checks has been proceeded.
     * <p>
     *     It is the second stage of the process of creating <class>trip</class>. Required step is when creating
     * <class>trip</class>, <class>tripUserStatus</class> with <class>userStatus</class> DRIVER to be created, so this is
     * how the relationship between <class>user</class> and <class>trip</class> is handled.They do not know for
     * each other, but <class>TripUserStatus</class> object knows for both of them.
     * <p>
     *     This method is specific and used only for creating initial status DRIVER for the owner of the <class>trip</class>.
     * This status is not expected to be changed furthermore.
     *
     * @param trip instance of the required <class>trip</class> for creating <class>tripUserStatus</class> object
     * @param loggedUserUsername <class>username</class> of the currently logged <class>user</class> extracted from the
     *      *                           security context thread
     * @return instance of the created <class>TripUserStatus</class> mapped as <class>TripUserStatusDtoResponse</class>
     */
    TripUserStatusDtoResponse createTripUserStatusAsDriver(Trip trip, String loggedUserUsername);

    void changeUserStatus(Long tripId, String passengerUsername,
                          String loggedUserUsername, UserStatus userStatus);

}

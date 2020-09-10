package com.telerik.carpooling.service.service.contract;

import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.enums.UserStatus;
import com.telerik.carpooling.model.Trip;
import com.telerik.carpooling.model.dto.TripUserStatusDtoResponse;

import java.util.List;

public interface TripUserStatusService {

    /**
     *     Fetches from the database all <class>tripUserStatuses</class> in which contained <class>trip</class> currently logged
     * user participates like driver or passenger and where <class>userStatus</class> is DRIVER.
     * <p>
     *     Parameter loggedUserUsername is trusted and is not checked as it is extracted from security context as a
     * currently logged <class>user</class>.
     *
     * @param loggedUserUsername <class>username</class> of the currently logged <class>user</class> extracted from the
     *                         security context thread
     * @return <class>List</class> with instances of the fetched <class>TripUserStatuses</class> mapped as <class>TripDtoResponses</class>
     */
    List<TripUserStatusDtoResponse> getUserOwnTripsWithDrivers(String loggedUserUsername);

    List<TripUserStatusDtoResponse> getAllTripUserStatuses(Integer pageEnd, Integer pageStart, TripStatus tripStatus, String origin,
                                                           String destination, String earliestDepartureTime, String latestDepartureTime,
                                                           Integer availablePlaces, Boolean smoking, Boolean pets, Boolean luggage, Boolean airConditioned);

    List<TripUserStatusDtoResponse> getTripUserStatuses(Long tripID);

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
     * <p>
     *     Transactional annotation is added to override class based behavior read only = true, with read only = false, as
     * this method is modifying the entity so we expect Hibernate to observe changes in the current Persistence Context
     * and include update at flush-time.
     *
     * @param trip instance of the required <class>trip</class> for creating <class>tripUserStatus</class> object
     * @param loggedUserUsername <class>username</class> of the currently logged <class>user</class> extracted from the
     *      *                           security context thread
     * @return instance of the created <class>TripUserStatus</class> mapped as <class>TripUserStatusDtoResponse</class>
     */
    TripUserStatusDtoResponse createTripUserStatusAsDriver(Trip trip, String loggedUserUsername);

    /**
     * Changes <class>UserStatus</class> of this passenger which <class>username</class> is passed as parameter.
     * <p>
     * In case the logged <class>user</class> does not belong to <class>trip</class> exception is thrown with relevant
     * message, except for PENDING <class>userStatus</class>. In case logged <class>user</class> is trying to change
     * passenger status and is not a driver exception is thrown with relevant message.
     * <p>
     * Various stages should be logically connected and if case there is mismatch exception is thrown with matching
     * message.
     * Accepted paths are PENDING-ACCEPTED-ABSENT or PENDING-ACCEPTED, CANCELED is accepted from a passenger, REJECTED
     * is accepted from a driver, ABSENT is accepted from a driver in case previous <class>userStatus</class> of
     * the passenger is ACCEPTED.
     * <p>
     * Transactional annotation is added to override class based behavior read only = true, with read only = false, as
     * this method is modifying the entity so we expect Hibernate to observe changes in the current Persistence Context
     * and include update at flush-time.
     *
     * @param tripId  the <class>modelId</class> of the <class>trip</class>
     * @param passengerUsername the <class>username</class> of this<class>user</class> which <class>userStatus</class>
     *                          is passed for change
     * @param loggedUserUsername <class>username</class> of the currently logged <class>user</class> extracted from
     *      *                           the security context thread
     * @param userStatus  the new <class>UserStatus</class> to be applied for this passenger
     */
    void changeUserStatus(Long tripId, String passengerUsername,
                          String loggedUserUsername, UserStatus userStatus);

    /**
     *     Helper method of a changeUserStatus method in <class>UserStatus</class> class. It is called when a passenger
     * is confirmed with <class>userStatus</class> ACCEPTED to adjust available seat down and change
     * <class>TripStatus</class> if necessary.
     *
     * @param trip instance of the required <class>trip</class> for adjusting its parameters
     */
    void adjustAvailablePlacesAndTripStatusWhenPassengerIsAccepted(Trip trip);
}

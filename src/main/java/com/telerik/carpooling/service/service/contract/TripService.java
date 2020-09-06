package com.telerik.carpooling.service.service.contract;

import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.exception.MyNotFoundException;
import com.telerik.carpooling.model.Trip;
import com.telerik.carpooling.model.dto.TripDtoEdit;
import com.telerik.carpooling.model.dto.TripDtoRequest;
import com.telerik.carpooling.model.dto.TripDtoResponse;

/**
 *
 *
 * @author Detelin Radev
 */
public interface TripService {

    /**
     * Creates <class>trip</class> from given DTO object, there is check for prerequisite that <class>user</class>
     * have to create <class>car</class> before be able to create <class>trip</class>, if the criteria is met,
     * <class>trip</class> is created, otherwise checked exception is thrown.
     * <p>
     * It is expected the input to be valid data, based on a validity check in the controller with annotation @Valid
     * and restrains upon the creation of the DTO object. However it that is not the case, validation criteria are as well
     * placed in the actual entity class as well, what would fire an exception if invalid data is provided.
     * <p>
     * Transactional annotation is added to override class based behavior read only = true, with read only = false, as
     * this method is modifying the entity so we expect Hibernate to observe changes in the current Persistence Context
     * and include update at flush-time.
     * Rollback on exception is set on Exception.class to override default behavior to do not rollback
     * on checked exceptions as we expect transaction to not succeed if <class>user</class> do not have <class>car</class>.
     *
     * @param tripDtoRequest     DTO that holds required data for creating <class>rip</class> object
     * @param loggedUserUsername <class>username</class> of the currently logged <class>user</class> extracted from the
     *                           security context thread
     * @return instance of the created object as <class>TripDtoResponse</class>
     * @throws MyNotFoundException throws checked exception if the <class>car</class> has not been created upon the
     *                             creating of the <class>trip</class>
     */
    Trip createTrip(TripDtoRequest tripDtoRequest, String loggedUserUsername) throws MyNotFoundException;

    /**
     * Updates <class>trip</class> from given DTO object, there is check if the logged <class>user</class> is the owner
     * of this <class>trip</class>, if the criteria is met, this <class>trip</class> is updated, otherwise
     * IllegalArgumentException exception is thrown as that is not the expected behavior.
     * <p>
     * It is expected the input to be valid data, based on a validity check in the controller with annotation @Valid
     * and restrains upon the creation of the DTO object. However it that is not the case, validation criteria are as well
     * placed in the actual entity class as well, what would fire an exception if invalid data is provided.
     * <p>
     * Transactional annotation is added to override class based behavior rollbackFor = RuntimeException.class, with
     * rollbackFor = Exception.class, as this method is throwing checked exception what is considered to be reason for
     * failing the transaction, what otherwise would not happen.
     *
     * @param tripDtoEdit        DTO that holds required data for updating <class>trip</class> object
     * @param loggedUserUsername <class>username</class> of the currently logged <class>user</class> extracted from
     *                           the security context thread
     * @return instance of the updated object as <class>TripDtoResponse</class>
     */
    TripDtoResponse updateTrip(TripDtoEdit tripDtoEdit, String loggedUserUsername);

    /**
     * Deletes softly <class>trip</class> with given trip <class>modelId</class>, there is check if the logged
     * <class>user</class> is the owner of this <class>trip</class> or <class>admin</class>,
     * if the criteria is met, the <class>trip</class> is retrieved from the database, otherwise
     * IllegalArgumentException exception is thrown as that is not the expected result.
     * <p>
     *
     * @param tripId             the <class>modelId</class> of the <class>trip</class>
     * @param loggedUserUsername <class>username</class> of the currently logged <class>user</class> extracted from
     *                           the security context thread
     */
    void deleteTrip(Long tripId, String loggedUserUsername);

    /**
     * Changes <class>TripStatus</class> of this <class>trip</class> which <class>modelId</class> is passed as parameter.
     * <p>
     * In case the logged <class>user</class> is not the owner of the <class>trip</class> exception is thrown with relevant message.
     * <p>
     * Various stages should be logically connected and if case there is mismatch exception is thrown with matching
     * message.
     * Accepted paths are AVAILABLE-BOOKED-ONGOING-DONE, CANCEL is accepted in all
     * stages except after DONE.
     * <p>
     *
     * @param tripId             the <class>modelId</class> of the <class>trip</class> to be deleted
     * @param loggedUserUsername <class>username</class> of the currently logged <class>user</class> extracted from
     *                           the security context thread
     * @param tripStatus         the new <class>TripStatus</class> to be applied for this <class>trip</class>
     */
    void changeTripStatus(Long tripId, String loggedUserUsername, TripStatus tripStatus);

    /**
     * Changes all PENDING <class>userStatuses</class> to REJECTED when this <class>trip</class> that is passed as
     * argument had its <class>tripStatus</class> changed from AVAILABLE to BOOKED, so users that were still
     * pending could be notified that are now rejected.
     *
     * @param trip instance of <class>trip</class> where all PENDING <class>userStatus</class> are to be changed
     */
    void changeAllLeftPendingUserStatusesToRejected(Trip trip);
}

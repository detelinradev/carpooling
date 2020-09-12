package com.telerik.carpooling.service.service.contract;

import com.telerik.carpooling.model.dto.FeedbackDtoResponse;

import java.util.Set;

public interface FeedbackService {


    /**
     *     Creates <class>Feedback</class> object from passed parameters.
     * <p>
     *     Validation is provided for all passed parameters, if tripId or receiverUsername are not valid exception is
     * thrown, parameter feedbackString is validated at controller and loggedUserUsername is extracted from security context
     * and therefore is considered safe.
     * <p>
     *     There is check if loggedUser and ratedUser both belong to the <class>trip</class> and one of them is
     * with <class>UserStatus</class> DRIVER, otherwise exception is thrown.
     * <p>
     *     Transactional annotation is added to override class based behavior read only = true, with read only = false, as
     * this method is modifying the entity so we expect Hibernate to observe changes in the current Persistence Context
     * and include update at flush-time.
     *
     * @param tripID  the <class>modelId</class> of the <class>trip</class>
     * @param loggedUserUsername <class>username</class> of the currently logged <class>user</class> extracted from
     *                           the security context thread
     * @param receiverUsername <class>username</class> of this<class>user</class> who will be left feedback
     * @param feedbackString string representation of field <class>feedback</class> of <class>Feedback</class> object
     */
    void leaveFeedback(Long tripID,String loggedUserUsername, String receiverUsername, String feedbackString) ;

    /**
     *     Gets all <class>Feedback</class> objects created for the given <class>user</class>.
     * <p>
     *     Validation is made for <class>username</class>, if not valid, exception si thrown.
     *
     * @param username <class>username</class> of this <class>user</class> which feedback will be fetched from database
     * @return <class>Set</class> with <class>FeedbackDtoResponse</class> objects created for the passed <class>user</class>
     */
    Set<FeedbackDtoResponse> getFeedback(String username);
}

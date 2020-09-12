package com.telerik.carpooling.service.service.contract;

import com.telerik.carpooling.model.dto.CommentDtoEdit;
import com.telerik.carpooling.model.dto.CommentDtoResponse;

import java.util.Set;

public interface CommentService {

    /**
     *     Creates <class>Comment</class> object from passed parameters.
     * <p>
     *     Validation is provided for all passed parameters, if tripId is not valid exception is
     * thrown, parameter message is validated at controller and loggedUserUsername is extracted from security context
     * and therefore is considered safe.
     * <p>
     *     Transactional annotation is added to override class based behavior read only = true, with read only = false, as
     * this method is modifying the entity so we expect Hibernate to observe changes in the current Persistence Context
     * and include update at flush-time.
     *
     * @param tripID the <class>modelId</class> of the <class>trip</class> where <class>comment</class> will be created
     * @param loggedUserUsername <class>username</class> of the currently logged <class>user</class> extracted from
     *                           the security context thread
     * @param message string representation of field <class>message</class> of <class>Comment</class> object
     * @return <class>CommentDtoResponse</class> instance of the created <class>Comment</class> object
     */
    CommentDtoResponse createComment(Long tripID, String loggedUserUsername, String message);

    /**
     *     Gets all <class>Comment</class> objects created for the given <class>trip</class>.
     * <p>
     *     Validation is made for parameter tripId, if it is not valid, exception si thrown.
     *
     * @param tripId the <class>modelId</class> of the <class>trip</class> which comments will be retrieved from database
     * @return <class>Set</class> with <class>CommentDtoResponse</class> objects retrieved from database for the passed
     * <class>trip</class>
     */
    Set<CommentDtoResponse> getComments(Long tripId);

    /**
     *   Deletes softly <class>Comment</class> with given <class>comment</class> <class>modelId</class>
     * <p>
     *     There is check if the logged <class>user</class> is the author of this <class>comment</class> or
     * <class>admin</class>, if the criteria is met, the <class>comment</class> is softly delete, otherwise
     * IllegalArgumentException exception is thrown as that is not the expected result.
     * <p>
     *     Validation is made for <class>comment</class> <class>modelId</class>, if it is not passed exception is thrown.
     * Parameter loggedUserUsername is trusted and is not checked as it is extracted from the security context as a
     * currently logged <class>user</class>.
     * <p>
     *     Transactional annotation is added to override class based behavior read only = true, with read only = false, as
     * this method is modifying the entity so we expect Hibernate to observe changes in the current Persistence Context
     * and include update at flush-time.
     *
     * @param commentId the <class>modelId</class> of the <class>comment</class> to be deleted
     * @param loggedUserUsername <class>username</class> of the currently logged <class>user</class> extracted from
     *                           the security context thread
     */
    void deleteComment(Long commentId, String loggedUserUsername);

    /**
     *     Updates <class>comment</class> from given DTO object.
     * <p>
     *     There is check if the logged <class>user</class> is the author of this <class>comment</class> or admin, if
     * the criteria is met, this <class>comment</class> is updated, otherwise exception is
     * thrown as that is not the expected behavior.
     * <p>
     *     It is expected the input to be valid data, based on a validity check in the controller with annotation @Valid
     * and restrains upon the creation of the DTO object. However it that is not the case, validation criteria are as well
     * placed in the actual entity class, what would fire an exception if invalid data is provided. Parameter
     * loggedUserUsername is extracted from security context and therefore is considered safe.
     * <p>
     *     Transactional annotation is added to override class based behavior read only = true, with read only = false, as
     * this method is modifying the entity so we expect Hibernate to observe changes in the current Persistence Context
     * and include update at flush-time.
     *
     * @param commentDtoEdit  DTO that holds required data for creating <class>Comment</class> object
     * @param loggedUserUsername loggedUserUsername <class>username</class> of the currently logged <class>user</class>
     *                           extracted from the security context thread
     * @return instance of the updated <class>comment</class> mapped as <class>CommentDtoResponse</class>
     */
    CommentDtoResponse updateComment(CommentDtoEdit commentDtoEdit, String loggedUserUsername);
}

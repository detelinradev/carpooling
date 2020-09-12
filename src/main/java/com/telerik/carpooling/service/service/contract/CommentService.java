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

    void deleteComment(Long id, String username);

    CommentDtoResponse updateComment(CommentDtoEdit commentDtoEdit, String username);
}

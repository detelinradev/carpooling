package com.telerik.carpooling.service.service.contract;

import com.telerik.carpooling.model.TripUserStatus;
import com.telerik.carpooling.model.User;

import java.util.List;

public interface RatingService {


    /**
     *     Creates <class>Rating</class> object from passed parameters.
     * <p>
     *     Validation is provided for all passed parameters, if tripId or ratedUserUsername are not valid exception is
     * thrown, parameter rating is validated at controller and loggedUserUsername is extracted from security context
     * and therefore is considered safe.
     * <p>
     *     There is check if loggedUser and ratedUser both belong to the <class>trip</class> and one of them is
     * with <class>UserStatus</class> DRIVER, otherwise exception is thrown.
     *
     * @param tripID  the <class>modelId</class> of the <class>trip</class>
     * @param loggedUserUsername <class>username</class> of the currently logged <class>user</class> extracted from
     *                           the security context thread
     * @param ratedUserUsername <class>username</class> of this<class>user</class> who will be rated
     * @param rating integer representation of field <class>rating</class> of <class>Rating</class> object
     */
    void createRating(Long tripID, String loggedUserUsername, String ratedUserUsername, Integer rating);

    /**
     *     Sets <class>ratingAsPassenger</class> or <class>ratingAsDriver</class> of the passed <class>user</class> based
     * on check if this passed <class>user</class> is with <class>UserStatus</class> DRIVER.
     * <p>
     *     There is check if parameters tripId and ratedUserUsername are valid, otherwise exception is thrown, parameter
     * rating has validation check in controller.
     *
     * @param tripId            the <class>modelId</class> of the <class>trip</class>
     * @param ratedUserUsername <class>username</class> of this<class>user</class> who will be rated
     * @param rating            integer representation of field <class>rating</class> of <class>Rating</class> object
     */
    void setUserRating(Long tripId, String ratedUserUsername, Integer rating);

    boolean doLoggedUserAndInteractedUserBothBelongToTripAndOneOfThemIsDriver
            (final User loggedUser, final User ratedUser, final List<TripUserStatus> tripUserStatuses);
}

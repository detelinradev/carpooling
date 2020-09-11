package com.telerik.carpooling.service.service.contract;

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

    void setUserRating(Long tripId, String passengerUsername, Integer rating);
}

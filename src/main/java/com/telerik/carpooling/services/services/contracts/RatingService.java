package com.telerik.carpooling.services.services.contracts;

import com.telerik.carpooling.models.User;

public interface RatingService {

    void loggingRating(User loggedUser, User ratedUser, int rating);
}

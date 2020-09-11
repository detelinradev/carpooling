package com.telerik.carpooling.service.service.contract;

import javassist.NotFoundException;

public interface RatingService {


    void createRating(Long tripID, String loggedUserUsername, String passengerUsername, Integer rating) throws NotFoundException;
    void setUserRating(Long tripId, String passengerUsername, Integer rating);
}

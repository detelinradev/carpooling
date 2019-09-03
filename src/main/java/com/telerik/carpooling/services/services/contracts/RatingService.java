package com.telerik.carpooling.services.services.contracts;

import javassist.NotFoundException;

public interface RatingService {


    void rateUser(Long tripID, String loggedUserUsername, String passengerUsername, Integer rating) throws NotFoundException;
}

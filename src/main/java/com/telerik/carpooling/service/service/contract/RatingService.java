package com.telerik.carpooling.service.service.contract;

import javassist.NotFoundException;

public interface RatingService {


    void rateUser(Long tripID, String loggedUserUsername, String passengerUsername, Integer rating) throws NotFoundException;
}

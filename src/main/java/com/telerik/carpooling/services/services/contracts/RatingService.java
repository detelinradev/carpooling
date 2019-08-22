package com.telerik.carpooling.services.services.contracts;

import com.telerik.carpooling.models.Rating;
import com.telerik.carpooling.models.User;

public interface RatingService {


    Rating rateUser(String tripID, User user,String passengerID, Integer rating);
}

package com.telerik.carpooling.services.services.contracts;

import com.telerik.carpooling.models.Rating;
import com.telerik.carpooling.models.User;

public interface RatingService {

    Rating rateDriver(String tripID, User user, int rating);

    Rating ratePassenger(String tripID, User user,String passengerID, int rating);
}

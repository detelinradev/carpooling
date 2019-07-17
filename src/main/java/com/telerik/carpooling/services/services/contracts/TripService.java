package com.telerik.carpooling.services.services.contracts;

import com.telerik.carpooling.models.Trip;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.TripDto;

public interface TripService {

    TripDto createTrip(TripDto trip, User driver);
    TripDto updateTrip(Trip trip, User driver);
}

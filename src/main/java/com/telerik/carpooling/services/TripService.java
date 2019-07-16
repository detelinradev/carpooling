package com.telerik.carpooling.services;

import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.TripDto;

public interface TripService {

    TripDto createTrip(TripDto trip, User driver);
}

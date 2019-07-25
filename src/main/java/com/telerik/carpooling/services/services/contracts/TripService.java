package com.telerik.carpooling.services.services.contracts;

import com.telerik.carpooling.enums.PassengerStatus;
import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.models.Trip;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.TripDtoEdit;
import com.telerik.carpooling.models.dtos.TripDtoRequest;
import com.telerik.carpooling.models.dtos.TripDtoResponse;

public interface TripService {

    Trip createTrip(TripDtoRequest trip, User driver);

    Trip updateTrip(TripDtoEdit trip);

    Trip addPassenger(String tripID, User user);

    Trip changePassengerStatus(String TripID,
                               User user, String passengerID, PassengerStatus passengerStatus);

    Trip changeTripStatus(String tripID,
                          User user, TripStatus tripStatus);

    TripDtoResponse getTrip(String tripID);
}


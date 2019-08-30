package com.telerik.carpooling.services.services.contracts;

import com.telerik.carpooling.enums.PassengerStatus;
import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.models.Trip;
import com.telerik.carpooling.models.User;

import java.util.List;

public interface TripService {

    Trip createTrip(Trip trip, User driver);

    Trip updateTrip(Trip trip, User user);

    Trip addPassenger(String tripID, User user);

    Trip changePassengerStatus(String TripID,
                               User user, String passengerID, PassengerStatus passengerStatus);

    Trip changeTripStatus(String tripID,
                          User user, TripStatus tripStatus);

    Trip getTrip(String tripID);

    List<Trip> getTrips(Integer pageEnd, Integer pageStart, String tripStatus, String origin,
                                   String destination, String earliestDepartureTime, String latestDepartureTime,
                                   String availablePlaces, String smoking, String pets, String luggage, String airConditioned);

    Trip deleteTrip(String tripId, User user);
}


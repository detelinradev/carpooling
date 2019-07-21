package com.telerik.carpooling.services.services.contracts;

import com.telerik.carpooling.enums.PassengerStatus;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.TripDtoRequest;
import com.telerik.carpooling.models.dtos.TripDtoResponse;

public interface TripService {

    TripDtoResponse createTrip(TripDtoRequest trip, User driver);

    TripDtoResponse updateTrip(TripDtoResponse trip);

    TripDtoResponse addPassenger(TripDtoResponse trip, User user);

    TripDtoResponse approvePassenger(TripDtoResponse trip, int passengerID);

    TripDtoResponse changePassengerStatus (TripDtoResponse tripDtoResponse,
                                           User passenger, PassengerStatus passengerStatus);

    TripDtoResponse rejectPassenger(TripDtoResponse tripDtoResponse,int passengerID);

    TripDtoResponse absentPassenger(TripDtoResponse tripDtoResponse,int passengerID);
}

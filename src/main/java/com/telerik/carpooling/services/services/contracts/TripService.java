package com.telerik.carpooling.services.services.contracts;

import com.telerik.carpooling.enums.PassengerStatus;
import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.TripDtoRequest;
import com.telerik.carpooling.models.dtos.TripDtoResponse;

public interface TripService {

    TripDtoResponse createTrip(TripDtoRequest trip, User driver);

    TripDtoResponse updateTrip(TripDtoResponse trip);

    TripDtoResponse addPassenger(TripDtoResponse trip, User user);

    TripDtoResponse changePassengerStatus (TripDtoResponse tripDtoResponse,
                                           User user, PassengerStatus passengerStatus);

    TripDtoResponse changeTripStatus (TripDtoResponse tripDtoResponse,
                                           User user, TripStatus tripStatus);

}

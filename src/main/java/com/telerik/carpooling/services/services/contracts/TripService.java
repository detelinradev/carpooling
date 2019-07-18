package com.telerik.carpooling.services.services.contracts;

import com.telerik.carpooling.models.Trip;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.TripDtoRequest;
import com.telerik.carpooling.models.dtos.TripDtoResponse;

import java.util.Optional;

public interface TripService {

    TripDtoResponse createTrip(TripDtoRequest trip, User driver);
    Optional<Trip> tripRate(TripDtoResponse tripResponseDto, User user, String userRole,
                      int ratedUserID, String ratedUserRole, int rating);
}

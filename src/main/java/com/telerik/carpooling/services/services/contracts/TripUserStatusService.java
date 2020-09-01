package com.telerik.carpooling.services.services.contracts;

import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.exceptions.MyNotFoundException;
import com.telerik.carpooling.models.dtos.TripUserStatusDtoResponse;

import java.util.List;

public interface TripUserStatusService {

    List<TripUserStatusDtoResponse> userOwnTripsWithDrivers(String username);

    List<TripUserStatusDtoResponse> getAllTripUserStatuses(Integer pageEnd, Integer pageStart, TripStatus tripStatus, String origin,
                                                           String destination, String earliestDepartureTime, String latestDepartureTime,
                                                           Integer availablePlaces, Boolean smoking, Boolean pets, Boolean luggage, Boolean airConditioned);

    List<TripUserStatusDtoResponse> getTripUserStatuses(Long tripID) throws MyNotFoundException;
}

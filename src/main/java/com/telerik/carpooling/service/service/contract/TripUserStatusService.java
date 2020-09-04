package com.telerik.carpooling.service.service.contract;

import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.exception.MyNotFoundException;
import com.telerik.carpooling.model.Trip;
import com.telerik.carpooling.model.dto.TripUserStatusDtoResponse;

import java.util.List;

public interface TripUserStatusService {

    List<TripUserStatusDtoResponse> userOwnTripsWithDrivers(String username);

    List<TripUserStatusDtoResponse> getAllTripUserStatuses(Integer pageEnd, Integer pageStart, TripStatus tripStatus, String origin,
                                                           String destination, String earliestDepartureTime, String latestDepartureTime,
                                                           Integer availablePlaces, Boolean smoking, Boolean pets, Boolean luggage, Boolean airConditioned);

    List<TripUserStatusDtoResponse> getTripUserStatuses(Long tripID) throws MyNotFoundException;

    List<TripUserStatusDtoResponse> getTripUserStatus(Long tripId) throws MyNotFoundException;

    TripUserStatusDtoResponse createTripUserStatus(Trip trip, String loggedUserUsername);


}

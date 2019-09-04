package com.telerik.carpooling.services.services.contracts;

import com.telerik.carpooling.enums.UserStatus;
import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.models.dtos.TripDtoEdit;
import com.telerik.carpooling.models.dtos.TripDtoRequest;
import com.telerik.carpooling.models.dtos.TripDtoResponse;
import com.telerik.carpooling.models.dtos.TripUserStatusDtoResponse;
import javassist.NotFoundException;

import java.util.List;

public interface TripService {

    TripDtoResponse createTrip(TripDtoRequest tripDtoRequest, String loggedUserUsername) throws NotFoundException;

    TripDtoResponse updateTrip(TripDtoEdit tripDtoEdit, String loggedUserUsername);

    void changeUserStatus(Long tripId, String passengerUsername,
                          String loggedUserUsername, UserStatus userStatus) throws NotFoundException;

    void changeTripStatus(Long tripId, String loggedUserUsername, TripStatus tripStatus) throws NotFoundException;

    TripDtoResponse getTrip(Long tripID) throws NotFoundException;

    List<TripDtoResponse> getTrips(Integer pageEnd, Integer pageStart, TripStatus tripStatus, String origin,
                                   String destination, String earliestDepartureTime, String latestDepartureTime,
                                   Integer availablePlaces, Boolean smoking, Boolean pets, Boolean luggage, Boolean airConditioned);

    void deleteTrip(Long tripId, String loggedUserUsername) throws NotFoundException;

    List<TripUserStatusDtoResponse> getTripUserStatus(Long tripId) throws NotFoundException;
}


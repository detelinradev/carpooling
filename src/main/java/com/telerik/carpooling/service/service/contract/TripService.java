package com.telerik.carpooling.service.service.contract;

import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.enums.UserStatus;
import com.telerik.carpooling.exception.MyNotFoundException;
import com.telerik.carpooling.model.Trip;
import com.telerik.carpooling.model.dto.TripDtoEdit;
import com.telerik.carpooling.model.dto.TripDtoRequest;
import com.telerik.carpooling.model.dto.TripDtoResponse;

public interface TripService {

    Trip createTrip(TripDtoRequest tripDtoRequest, String loggedUserUsername) throws MyNotFoundException;

    TripDtoResponse updateTrip(TripDtoEdit tripDtoEdit, String loggedUserUsername);

    void changeUserStatus(Long tripId, String passengerUsername,
                          String loggedUserUsername, UserStatus userStatus) throws MyNotFoundException;

    void changeTripStatus(Long tripId, String loggedUserUsername, TripStatus tripStatus) throws MyNotFoundException;

    void deleteTrip(Long tripId, String loggedUserUsername);

//    TripDtoResponse getTrip(Long tripID) throws MyNotFoundException;

//    List<TripDtoResponse> getTrips(Integer pageEnd, Integer pageStart, TripStatus tripStatus, String origin,
//                                   String destination, String earliestDepartureTime, String latestDepartureTime,
//                                   Integer availablePlaces, Boolean smoking, Boolean pets, Boolean luggage, Boolean airConditioned);




}


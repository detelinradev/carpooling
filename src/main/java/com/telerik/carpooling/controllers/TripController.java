package com.telerik.carpooling.controllers;

import com.telerik.carpooling.enums.UserStatus;
import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.exceptions.MyNotFoundException;
import com.telerik.carpooling.models.dtos.TripDtoEdit;
import com.telerik.carpooling.models.dtos.TripDtoRequest;
import com.telerik.carpooling.models.dtos.TripDtoResponse;
import com.telerik.carpooling.models.dtos.TripUserStatusDtoResponse;
import com.telerik.carpooling.services.services.contracts.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("trips")
public class TripController {

    private final TripService tripService;
    private final TripUserStatusService tripUserStatusService;

    @PostMapping
    public ResponseEntity<TripDtoResponse> createTrip(@RequestBody final TripDtoRequest tripDtoRequest,
                                                      final Authentication authentication) throws MyNotFoundException {

        return ResponseEntity.ok().body(tripService.createTrip(tripDtoRequest,authentication.getName()));
    }

    @GetMapping
    public ResponseEntity<List<TripUserStatusDtoResponse>> getAllTripUserStatuses(@RequestParam(value = "_end", required = false)
                                            final Integer pageNumber,
                                                          @RequestParam(value = "_start", required = false)
                                             final Integer pageSize,
                                                          @RequestParam(value = "status", required = false)
                                            final TripStatus tripStatus,
                                                          @RequestParam(value = "origin", required = false)
                                            final  String origin,
                                                          @RequestParam(value = "destination", required = false)
                                             final String destination,
                                                          @RequestParam(value = "earliestDepartureTime", required = false)
                                             final String earliestDepartureTime,
                                                          @RequestParam(value = "latestDepartureTime", required = false)
                                             final String latestDepartureTime,
                                                          @RequestParam(value = "availablePlaces", required = false)
                                            final Integer availablePlaces,
                                                          @RequestParam(value = "smokingAllowed", required = false)
                                             final Boolean smoking,
                                                          @RequestParam(value = "petsAllowed", required = false)
                                             final Boolean pets,
                                                          @RequestParam(value = "luggageAllowed", required = false)
                                             final Boolean luggage,
                                                          @RequestParam(value = "airConditioned", required = false)
                                             final Boolean airConditioned)

    {
        return ResponseEntity.ok().body(tripUserStatusService.getAllTripUserStatuses(pageNumber, pageSize, tripStatus, origin, destination,
                earliestDepartureTime, latestDepartureTime, availablePlaces, smoking, pets, luggage,airConditioned));
    }

    @GetMapping(value = "/{tripId}")
    public ResponseEntity<List<TripUserStatusDtoResponse>> getTripUserStatuses(@PathVariable final Long tripId) throws MyNotFoundException {

        return ResponseEntity.ok().body(tripUserStatusService.getTripUserStatuses(tripId));
    }

    @GetMapping(value = "/myTrips")
    public ResponseEntity<List<TripUserStatusDtoResponse>> getMyTrips(final Authentication authentication) {

        return ResponseEntity.ok().body(tripUserStatusService.userOwnTripsWithDrivers(authentication.getName()));
    }

    @GetMapping(value = "/TripUserStatus/{tripId}")
    public ResponseEntity<List<TripUserStatusDtoResponse>> getTripUserStatus(@PathVariable final Long tripId) throws MyNotFoundException {

        return ResponseEntity.ok().body(tripService.getTripUserStatus(tripId));
    }

    @PutMapping
    public ResponseEntity<TripDtoResponse> updateTrip(@Valid @RequestBody final TripDtoEdit tripDtoEdit,final Authentication authentication) {

        return ResponseEntity.ok().body(tripService.updateTrip(tripDtoEdit,authentication.getName()));
    }

    @PatchMapping(value = "/{tripId}")
    public ResponseEntity<Void> changeTripStatus(@PathVariable final Long tripId,
                                                   final Authentication authentication,
                                                   @RequestParam(value = "status") TripStatus tripStatus) throws MyNotFoundException {

        tripService.changeTripStatus(tripId,authentication.getName(),tripStatus);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/{tripId}/passengers/{passengerUsername}")
    public ResponseEntity<Void> changeUserStatus(@PathVariable final Long tripId,
                                                   @PathVariable final String passengerUsername,
                                                   final Authentication authentication,
                                                   @RequestParam(value = "status") UserStatus userStatus) throws MyNotFoundException {

        tripService.changeUserStatus(tripId,passengerUsername,authentication.getName(), userStatus);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{tripId}/delete")
    public ResponseEntity<Void> deleteTrip(@PathVariable final Long tripId,
                                        final Authentication authentication) throws MyNotFoundException {

        tripService.deleteTrip(tripId,authentication.getName());
        return ResponseEntity.ok().build();
    }
}
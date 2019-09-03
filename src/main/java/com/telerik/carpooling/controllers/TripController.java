package com.telerik.carpooling.controllers;

import com.telerik.carpooling.enums.UserStatus;
import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.models.dtos.TripDtoEdit;
import com.telerik.carpooling.models.dtos.TripDtoRequest;
import com.telerik.carpooling.models.dtos.TripDtoResponse;
import com.telerik.carpooling.services.services.contracts.*;
import javassist.NotFoundException;
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

    @PostMapping
    public ResponseEntity<TripDtoResponse> createTrip(@Valid @RequestBody final TripDtoRequest tripDtoRequest,
                                                      final Authentication authentication) throws NotFoundException {

        return ResponseEntity.ok().body(tripService.createTrip(tripDtoRequest,authentication.getName()));
    }

    @GetMapping
    public ResponseEntity<List<TripDtoResponse>> getTrips(@RequestParam(value = "_end", required = false)
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
        return ResponseEntity.ok().body(tripService.getTrips(pageNumber, pageSize, tripStatus, origin, destination,
                earliestDepartureTime, latestDepartureTime, availablePlaces, smoking, pets, luggage,airConditioned));
    }

    @GetMapping(value = "/{tripId}")
    public ResponseEntity<TripDtoResponse> getTrip(@PathVariable final Long tripId) throws NotFoundException {

        return ResponseEntity.ok().body(tripService.getTrip(tripId));
    }

    @PutMapping
    public ResponseEntity<TripDtoResponse> updateTrip(@Valid @RequestBody final TripDtoEdit tripDtoEdit,final Authentication authentication) {

        return ResponseEntity.ok().body(tripService.updateTrip(tripDtoEdit,authentication.getName()));
    }

    @PatchMapping(value = "/{tripId}")
    public ResponseEntity<Void> changeTripStatus(@PathVariable final Long tripId,
                                                   final Authentication authentication,
                                                   @RequestParam(value = "status") TripStatus tripStatus) throws NotFoundException {

        tripService.changeTripStatus(tripId,authentication.getName(),tripStatus);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/{tripId}/passengers/{passengerUsername}")
    public ResponseEntity<Void> changeUserStatus(@PathVariable final Long tripId,
                                                   @PathVariable final String passengerUsername,
                                                   final Authentication authentication,
                                                   @RequestParam(value = "status") UserStatus userStatus) throws NotFoundException {

        tripService.changeUserStatus(tripId,passengerUsername,authentication.getName(), userStatus);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/{tripId}/delete")
    public ResponseEntity<Void> deleteTrip(@PathVariable final Long tripId,
                                        final Authentication authentication) throws NotFoundException {

        tripService.deleteTrip(tripId,authentication.getName());
        return ResponseEntity.ok().build();
    }
}
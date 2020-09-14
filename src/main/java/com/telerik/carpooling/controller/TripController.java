package com.telerik.carpooling.controller;

import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.enums.UserStatus;
import com.telerik.carpooling.exception.MyNotFoundException;
import com.telerik.carpooling.model.dto.TripDtoEdit;
import com.telerik.carpooling.model.dto.TripDtoRequest;
import com.telerik.carpooling.model.dto.TripDtoResponse;
import com.telerik.carpooling.model.dto.TripUserStatusDtoResponse;
import com.telerik.carpooling.service.service.contract.TripService;
import com.telerik.carpooling.service.service.contract.TripUserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@CrossOrigin(maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("trips")
@Validated
public class TripController {

    private final TripService tripService;
    private final TripUserStatusService tripUserStatusService;

    @PostMapping
    public ResponseEntity<TripUserStatusDtoResponse> createTrip(@Valid @RequestBody final TripDtoRequest tripDtoRequest)
            throws MyNotFoundException {

        String loggedUserName = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.ok()
                .body(tripUserStatusService.createTripUserStatusAsDriver(
                        tripService.createTrip(tripDtoRequest, loggedUserName), loggedUserName));
    }

    @GetMapping
    public ResponseEntity<List<TripUserStatusDtoResponse>> getAllTripUserStatuses(
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") final Integer pageNumber,
            @RequestParam(value = "_start", required = false, defaultValue = "10") final Integer pageSize,
            @RequestParam(value = "status", required = false, defaultValue = "AVAILABLE") final TripStatus tripStatus,
            @RequestParam(value = "origin", required = false) final String origin,
            @RequestParam(value = "destination", required = false) final String destination,
            @RequestParam(value = "earliestDepartureTime", required = false) final String earliestDepartureTime,
            @RequestParam(value = "latestDepartureTime", required = false) final String latestDepartureTime,
            @RequestParam(value = "availablePlaces", required = false)
            @Min(value = 1, message = "Available places should be at least one")
            @Max(value = 4, message = "Available places should be at most four") final Integer availablePlaces,
            @RequestParam(value = "smokingAllowed", required = false) final Boolean smoking,
            @RequestParam(value = "petsAllowed", required = false) final Boolean pets,
            @RequestParam(value = "luggageAllowed", required = false) final Boolean luggage,
            @RequestParam(value = "airConditioned", required = false) final Boolean airConditioned) {

        return ResponseEntity.ok().body(tripUserStatusService.getAllTripsWithDrivers(pageNumber, pageSize, tripStatus,
                origin, destination, earliestDepartureTime, latestDepartureTime, availablePlaces, smoking, pets,
                luggage, airConditioned));
    }

    @GetMapping(value = "/{tripId}")
    public ResponseEntity<List<TripUserStatusDtoResponse>> getAllTripUserStatusesForATrip(@PathVariable @NotNull final Long tripId) {

        return ResponseEntity.ok().body(tripUserStatusService.getCurrentTripUserStatusForAllUsersInATrip(tripId));
    }

    @GetMapping(value = "/myTrips")
    public ResponseEntity<List<TripUserStatusDtoResponse>> getMyTrips() {

        String loggedUserName = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.ok().body(tripUserStatusService.getUserOwnTripsWithDrivers(loggedUserName));
    }

    @PutMapping
    public ResponseEntity<TripDtoResponse> updateTrip(@Valid @RequestBody final TripDtoEdit tripDtoEdit) {

        String loggedUserName = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.ok().body(tripService.updateTrip(tripDtoEdit, loggedUserName));
    }

    @PatchMapping(value = "/{tripId}")
    public ResponseEntity<Void> changeTripStatus(@PathVariable @NotNull final Long tripId,
                                                 @RequestParam(value = "status") @NotNull TripStatus tripStatus) {

        String loggedUserName = SecurityContextHolder.getContext().getAuthentication().getName();

        tripService.changeTripStatus(tripId, loggedUserName, tripStatus);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/{tripId}/passengers/{passengerUsername}")
    public ResponseEntity<Void> changeUserStatus(@PathVariable @NotNull final Long tripId,
                                                 @PathVariable @NotNull final String passengerUsername,
                                                 @RequestParam(value = "status")@NotNull UserStatus userStatus) {

        String loggedUserName = SecurityContextHolder.getContext().getAuthentication().getName();

        tripUserStatusService.changeUserStatus(tripId, passengerUsername, loggedUserName, userStatus);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/{tripId}/delete")
    public ResponseEntity<Void> deleteTrip(@PathVariable @NotNull final Long tripId) {

        String loggedUserName = SecurityContextHolder.getContext().getAuthentication().getName();

        tripService.deleteTrip(tripId, loggedUserName);
        return ResponseEntity.ok().build();
    }
}
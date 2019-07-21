package com.telerik.carpooling.controllers;

import com.telerik.carpooling.enums.PassengerStatus;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.TripDtoRequest;
import com.telerik.carpooling.models.dtos.TripDtoResponse;
import com.telerik.carpooling.repositories.UserRepository;
import com.telerik.carpooling.security.AuthenticationService;
import com.telerik.carpooling.services.services.contracts.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@CrossOrigin(maxAge = 3600)
@RestController
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    @PostMapping(value = "/trips")
    public ResponseEntity<TripDtoResponse> createTrip(@Valid @RequestBody final TripDtoRequest trip,
                                                      final HttpServletRequest req) {

        return Optional
                .ofNullable(tripService.createTrip(trip, userRepository.findFirstByUsername(
                        authenticationService.getUsername(req))))
                .map(tripResponseDto -> ResponseEntity.status(HttpStatus.CREATED).body(tripResponseDto))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping(value = "/trips")
    public ResponseEntity<TripDtoResponse> updateTrip(@Valid @RequestBody final TripDtoResponse trip) {

        return Optional
                .ofNullable(tripService.updateTrip(trip))
                .map(tripResponseDto -> ResponseEntity.ok().body(tripResponseDto))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/passengers")
    public ResponseEntity<?> addPassenger(@Valid @RequestBody final TripDtoResponse trip,
                                                        HttpServletRequest req) {
        return Optional
                .ofNullable(tripService.addPassenger(trip, userRepository.findFirstByUsername(
                        authenticationService.getUsername(req))))
                .map(k -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping(value = "/passengerOK")
    public ResponseEntity<TripDtoResponse> approvePassenger(@Valid @RequestBody final TripDtoResponse trip,
                                                            @RequestParam(value = "passengerID") int passengerID) {

        return Optional
                .ofNullable(tripService.approvePassenger(trip, passengerID))
                .map(tripDtoResponse -> ResponseEntity.ok().body(tripDtoResponse))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping(value = "/passengerStatus")
    public ResponseEntity<?> changePassengerStatus(@Valid @RequestBody final TripDtoResponse trip,
                                                      HttpServletRequest req,
                                                      @Valid @RequestParam(value = "status") PassengerStatus passengerStatus){

        return Optional
                .ofNullable(tripService.changePassengerStatus(trip, userRepository.findFirstByUsername(
                        authenticationService.getUsername(req)), passengerStatus))
                .map(k -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
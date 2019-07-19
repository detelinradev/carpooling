package com.telerik.carpooling.controllers;

import com.telerik.carpooling.models.dtos.TripDtoRequest;
import com.telerik.carpooling.models.dtos.TripDtoResponse;
import com.telerik.carpooling.repositories.UserRepository;
import com.telerik.carpooling.security.AuthenticationService;
import com.telerik.carpooling.services.services.contracts.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@CrossOrigin(maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/trip")
public class TripController {

    private final TripService tripService;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    @PostMapping(value = "/trip")
    public ResponseEntity<TripDtoResponse> createTrip(@Valid @RequestBody final TripDtoRequest trip,
                                                      final HttpServletRequest req) {

        return Optional
                .ofNullable(tripService.createTrip(trip, userRepository.findFirstByUsername(
                        authenticationService.getUsername(req))))
                .map(tripResponseDto -> ResponseEntity.ok().body(tripResponseDto))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping(value = "/trip")
    public ResponseEntity<TripDtoResponse> updateTrip(@Valid @RequestBody final TripDtoResponse trip) {

        return Optional
                .ofNullable(tripService.updateTrip(trip))
                .map(tripResponseDto -> ResponseEntity.ok().body(tripResponseDto))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping(value = "/rate")
    public ResponseEntity<TripDtoResponse> rateTrip(@Valid @RequestBody final TripDtoResponse trip,
                                                    final HttpServletRequest req,
                                                    @RequestParam(value = "userRole") String userRole,
                                                    @RequestParam(value = "ratedUserID") int ratedUserID,
                                                    @RequestParam(value = "ratedUserRole") String ratedUserROle,
                                                    @RequestParam(value = "rating") int rating) {

        return Optional
                .ofNullable(tripService.rateTrip(trip,userRepository.findFirstByUsername(
                        authenticationService.getUsername(req)), userRole, ratedUserID, ratedUserROle, rating))
                .map(tripDtoResponse -> ResponseEntity.ok().body(tripDtoResponse))
                .orElseGet(() -> ResponseEntity.badRequest().build());
        // ResponseEntity.of(tripService.rateTrip(trip, userRole, ratedUserID, ratedUserROle, rating));
    }

    @PutMapping(value = "/passenger")
    public ResponseEntity<TripDtoResponse> addPassenger(@Valid @RequestBody final TripDtoResponse trip,
                                                        HttpServletRequest req) {
        return Optional
                .ofNullable(tripService.addPassenger(trip, userRepository.findFirstByUsername(
                        authenticationService.getUsername(req))))
                .map(tripDtoResponse -> ResponseEntity.ok().body(tripDtoResponse))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping(value = "/passengerOK")
    public ResponseEntity<TripDtoResponse> approvePassenger(@Valid @RequestBody final TripDtoResponse trip,
                                                            @RequestParam(value = "passengerID") int passengerID) {

        return Optional
                .ofNullable(tripService.approvePassenger(trip, passengerID))
                .map(tripDtoResponse -> ResponseEntity.ok().body(tripDtoResponse))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
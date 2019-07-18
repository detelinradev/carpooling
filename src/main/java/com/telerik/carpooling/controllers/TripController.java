package com.telerik.carpooling.controllers;

import com.telerik.carpooling.models.Trip;
import com.telerik.carpooling.models.dtos.TripDtoRequest;
import com.telerik.carpooling.models.dtos.TripDtoResponse;
import com.telerik.carpooling.models.dtos.dtos.mapper.DtoMapper;
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

    private final DtoMapper dtoMapper;
    private final TripService tripService;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    @PostMapping(value = "/trip")
    public ResponseEntity<TripDtoResponse> createTrip(@Valid @RequestBody final TripDtoRequest trip, final HttpServletRequest req) {

        return Optional
                .ofNullable(tripService.createTrip(trip, userRepository.findFirstByUsername(
                        authenticationService.getUsername(req))))
                .map(tripResponseDto -> ResponseEntity.ok().body(tripResponseDto))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping(value = "/rate")
    public ResponseEntity<Trip> tripRate(@Valid @RequestBody final TripDtoResponse trip,
                                         final HttpServletRequest req,
                                         @RequestParam(value = "userRole") String userRole,
                                         @RequestParam(value = "ratedUserID") int ratedUserID,
                                         @RequestParam(value = "ratedUserRole") String ratedUserROle,
                                         @RequestParam(value = "rating")int rating) {

        return ResponseEntity.of(tripService.tripRate(trip, userRepository.findFirstByUsername(
                authenticationService.getUsername(req)), userRole, ratedUserID, ratedUserROle,rating));
//        Optional
//                .ofNullable(tripService.tripRate(trip, userRepository.findFirstByUsername(
//                        authenticationService.getUsername(req)), userRole, ratedUserID, ratedUserROle,rating))
        // .map(tripResponseDto -> ResponseEntity.ok().body(tripResponseDto))
        // .orElseGet(() -> ResponseEntity.badRequest().build());
    }


}

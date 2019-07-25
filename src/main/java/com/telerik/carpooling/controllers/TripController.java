package com.telerik.carpooling.controllers;

import com.telerik.carpooling.enums.PassengerStatus;
import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.models.Trip;
import com.telerik.carpooling.models.dtos.TripDtoEdit;
import com.telerik.carpooling.models.dtos.TripDtoRequest;
import com.telerik.carpooling.models.dtos.TripDtoResponse;
import com.telerik.carpooling.repositories.TripRepository;
import com.telerik.carpooling.repositories.UserRepository;
import com.telerik.carpooling.services.services.contracts.CommentService;
import com.telerik.carpooling.services.services.contracts.TripService;
import com.telerik.carpooling.services.services.contracts.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;

@CrossOrigin(maxAge = 3600)
@RestController
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;
    private final TripRepository tripRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final CommentService commentService;

    @PostMapping(value = "/trips")
    public ResponseEntity<?> createTrip(@Valid @RequestBody final TripDtoRequest trip,
                                        final Authentication authentication) {

        return Optional
                .ofNullable(tripService.createTrip(trip, userRepository.findFirstByUsername(
                        authentication.getName())))
                .map(k -> ResponseEntity.status(HttpStatus.CREATED).build())
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping(value = "/trips")
    public ResponseEntity<?> updateTrip(@Valid @RequestBody final TripDtoEdit tripDtoEdit,
                                        final Authentication authentication,
                                        HttpServletResponse httpServletResponse) throws IOException {
        Optional<Trip> trip = tripRepository.findById(tripDtoEdit.getId());
        if (trip.isPresent()) {
            if (!trip.get().getCreator().equals(authentication.getName()))
                httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
        return Optional
                .ofNullable(tripService.updateTrip(tripDtoEdit))
                .map(tripResponseDto -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping(value = "/trips/{tripID}")
    public ResponseEntity<?> changeTripStatus(@PathVariable final String tripID,
                                              final Authentication authentication,
                                              @RequestParam(value = "status") TripStatus tripStatus) throws IOException {


        return Optional
                .ofNullable(tripService.changeTripStatus(tripID,
                        userRepository.findFirstByUsername(authentication.getName()), tripStatus))
                .map(k -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/trips/{id}")
    public ResponseEntity<TripDtoResponse> getTrips(@PathVariable String id) {

        return Optional
                .ofNullable(tripService.getTrip(id))
                .map(tripDtoResponse -> ResponseEntity.ok().body(tripDtoResponse))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/trips/{id}/passengers")
    public ResponseEntity<?> addPassenger(@PathVariable final String id,
                                          final Authentication authentication) {
        return Optional
                .ofNullable(tripService.addPassenger(id, userRepository.findFirstByUsername(
                        authentication.getName())))
                .map(k -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping(value = "/trips/{tripId}/passengers/{passengerId}")
    public ResponseEntity<?> changePassengerStatus(@PathVariable final String tripId,
                                                   @PathVariable final String passengerId,
                                                   final Authentication authentication,
                                                   @RequestParam(value = "status") PassengerStatus passengerStatus) {
        return Optional
                .ofNullable(tripService.changePassengerStatus(tripId, userRepository.findFirstByUsername(
                        authentication.getName()), passengerId, passengerStatus))
                .map(k -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(value = "trips/{id}/comments")
    public ResponseEntity<?> createComment(@PathVariable final String id,
                                           final Authentication authentication,
                                           @RequestParam(value = "comment") final String message){
        return Optional
                .ofNullable(commentService.createComment( id, userRepository.findFirstByUsername(
                        authentication.getName()), message))
                .map(commentDto -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping(value = "/trips/{id}/driver/rate")
    public ResponseEntity<?> rateDriver(@PathVariable final String id,
                                      final Authentication authentication,
                                      @RequestBody int rating) {

        return Optional
                .ofNullable(userService.rateDriver(id,userRepository.findFirstByUsername(
                        authentication.getName()), rating))
                .map(tripDtoResponse -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping(value = "/trips/{tripId}/passengers/{passengerId}/rate")
    public ResponseEntity<?> ratePassenger(@PathVariable final String tripId,
                                           @PathVariable final String passengerId,
                                        final Authentication authentication,
                                        @RequestBody int rating) {

        return Optional
                .ofNullable(userService.ratePassenger(tripId,userRepository.findFirstByUsername(
                        authentication.getName()),passengerId, rating))
                .map(tripDtoResponse -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping(value = "/feedback")
    public ResponseEntity<?> leaveFeedback(@Valid @RequestBody final TripDtoResponse trip,
                                           final Authentication authentication,
                                           @RequestParam(value = "userRole") String userRole,
                                           @RequestParam(value = "userToGetFeedbackId") int ratedUserID,
                                           @RequestParam(value = "userToGetFeedbackRole") String ratedUserROle,
                                           @RequestParam(value = "feedback") String feedback) {

        return Optional
                .ofNullable(userService.leaveFeedback(trip,userRepository.findFirstByUsername(
                        authentication.getName()),userRole, ratedUserID, ratedUserROle, feedback))
                .map(tripDtoResponse -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
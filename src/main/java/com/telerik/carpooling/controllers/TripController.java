package com.telerik.carpooling.controllers;

import com.telerik.carpooling.enums.PassengerStatus;
import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.models.Trip;
import com.telerik.carpooling.models.dtos.TripDtoEdit;
import com.telerik.carpooling.models.dtos.TripDtoRequest;
import com.telerik.carpooling.models.dtos.TripDtoResponse;
import com.telerik.carpooling.models.dtos.dtos.mapper.DtoMapper;
import com.telerik.carpooling.repositories.TripRepository;
import com.telerik.carpooling.repositories.UserRepository;
import com.telerik.carpooling.services.services.contracts.CommentService;
import com.telerik.carpooling.services.services.contracts.RatingService;
import com.telerik.carpooling.services.services.contracts.TripService;
import com.telerik.carpooling.services.services.contracts.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@CrossOrigin(maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("trips")
public class TripController {

    private final TripService tripService;
    private final TripRepository tripRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final CommentService commentService;
    private final RatingService ratingService;
    private final DtoMapper dtoMapper;

    @GetMapping
    public ResponseEntity<?> getTrips(@RequestParam(value = "_end", required = false)
                                                                  Integer pageNumber,
                                                          @RequestParam(value = "_start", required = false)
                                                                  Integer pageSize,
                                                          @RequestParam(value = "status", required = false)
                                                                  String tripStatus,
                                                          @RequestParam(value = "driver", required = false)
                                                                  String driverUsername,
                                                          @RequestParam(value = "origin", required = false)
                                                                  String origin,
                                                          @RequestParam(value = "destination", required = false)
                                                                  String destination,
                                                          @RequestParam(value = "earliestDepartureTime", required = false)
                                                                  String earliestDepartureTime,
                                                          @RequestParam(value = "latestDepartureTime", required = false)
                                                                  String latestDepartureTime,
                                                          @RequestParam(value = "availablePlaces", required = false)
                                                                  String availablePlaces,
                                                          @RequestParam(value = "smoking", required = false)
                                                                  String smoking,
                                                          @RequestParam(value = "pets", required = false)
                                                                  String pets,
                                                          @RequestParam(value = "luggage", required = false)
                                                                  String luggage) {

        return Optional
                .ofNullable(dtoMapper.tripToDtoList(tripService.getTrips(pageNumber, pageSize, tripStatus, driverUsername, origin, destination,
                        earliestDepartureTime, latestDepartureTime, availablePlaces, smoking, pets, luggage)))
                .map(tripDtoResponse -> ResponseEntity.ok().body(tripDtoResponse))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<?> createTrip(@Valid @RequestBody final Trip trip,
                                        final Authentication authentication)  {

        return Optional
                .ofNullable(tripService.createTrip(trip, userRepository.findFirstByUsername(
                        authentication.getName())))
                .map(k -> ResponseEntity.status(HttpStatus.CREATED).build())
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping
    public ResponseEntity<?> updateTrip(@Valid @RequestBody final TripDtoEdit tripDtoEdit,
                                        final Authentication authentication,
                                        HttpServletResponse httpServletResponse) throws IOException {
        Optional<Trip> trip = tripRepository.findById(tripDtoEdit.getModelId());
        if (trip.isPresent()) {
            if (!trip.get().getCreator().equals(authentication.getName()))
                httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
        return Optional
                .ofNullable(tripService.updateTrip(tripDtoEdit))
                .map(tripResponseDto -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping(value = "/{tripID}")
    public ResponseEntity<?> changeTripStatus(@PathVariable final String tripID,
                                              final Authentication authentication,
                                              @RequestParam(value = "status") TripStatus tripStatus) {


        return Optional
                .ofNullable(tripService.changeTripStatus(tripID,
                        userRepository.findFirstByUsername(authentication.getName()), tripStatus))
                .map(k -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getTrips(@PathVariable String id) {

        return Optional
                .ofNullable(dtoMapper.objectToDto(tripService.getTrip(id)))
                .map(tripDtoResponse -> ResponseEntity.ok().body(tripDtoResponse))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/myTrips")
    public ResponseEntity<?> getUserOwnTrips(Authentication authentication) {
        return Optional
                .ofNullable(userService.getUserOwnTrips(
                        authentication.getName()))
                .map(user -> ResponseEntity.ok().body(user))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PostMapping(value = "/{id}/passengers")
    public ResponseEntity<?> addPassenger(@PathVariable final String id,
                                          final Authentication authentication) {
        return Optional
                .ofNullable(tripService.addPassenger(id, userRepository.findFirstByUsername(
                        authentication.getName())))
                .map(k -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping(value = "/{tripId}/passengers/{passengerId}")
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

    @PostMapping(value = "/{id}/comments")
    public ResponseEntity<?> createComment(@PathVariable final String id,
                                           final Authentication authentication,
                                           @RequestParam(value = "comment") final String message) {
        return Optional
                .ofNullable(commentService.createComment(id, userRepository.findFirstByUsername(
                        authentication.getName()), message))
                .map(commentDto -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping(value = "/{id}/driver/rate")
    public ResponseEntity<?> rateDriver(@PathVariable final String id,
                                        final Authentication authentication,
                                        @RequestBody Integer rating) {
        return Optional
                .ofNullable(ratingService.rateDriver(id, userRepository.findFirstByUsername(
                        authentication.getName()), rating))
                .map(tripDtoResponse -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping(value = "/{tripId}/passengers/{passengerId}/rate")
    public ResponseEntity<?> ratePassenger(@PathVariable final String tripId,
                                           @PathVariable final String passengerId,
                                           final Authentication authentication,
                                           @RequestBody Integer rating) {

        return Optional
                .ofNullable(ratingService.ratePassenger(tripId, userRepository.findFirstByUsername(
                        authentication.getName()), passengerId, rating))
                .map(tripDtoResponse -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping(value = "/{tripId}/passengers/{passengerId}/feedback")
    public ResponseEntity<?> leaveFeedbackPassenger(@PathVariable final String tripId,
                                                    @PathVariable final String passengerId,
                                                    final Authentication authentication,
                                                    @RequestBody String feedback) {

        return Optional
                .ofNullable(userService.leaveFeedbackPassenger(tripId, userRepository.findFirstByUsername(
                        authentication.getName()), passengerId, feedback))
                .map(tripDtoResponse -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping(value = "/{id}/driver/feedback")
    public ResponseEntity<?> leaveFeedbackDriver(@PathVariable final String id,
                                                 final Authentication authentication,
                                                 @RequestBody String feedback) {

        return Optional
                .ofNullable(userService.leaveFeedbackDriver(id, userRepository.findFirstByUsername(
                        authentication.getName()), feedback))
                .map(tripDtoResponse -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
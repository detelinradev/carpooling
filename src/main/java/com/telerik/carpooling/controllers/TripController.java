package com.telerik.carpooling.controllers;

import com.telerik.carpooling.enums.PassengerStatus;
import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.models.dtos.TripDtoEdit;
import com.telerik.carpooling.models.dtos.TripDtoRequest;
import com.telerik.carpooling.models.dtos.TripDtoResponse;
import com.telerik.carpooling.models.dtos.dtos.mapper.DtoMapper;
import com.telerik.carpooling.services.services.contracts.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@CrossOrigin(maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("trips")
public class TripController {

    private final TripService tripService;
    private final UserService userService;
    private final CommentService commentService;
    private final DtoMapper dtoMapper;

    @GetMapping
    public ResponseEntity<?> getTrips(@RequestParam(value = "_end", required = false)
                                              Integer pageNumber,
                                      @RequestParam(value = "_start", required = false)
                                              Integer pageSize,
                                      @RequestParam(value = "status", required = false)
                                              String tripStatus,
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
                                      @RequestParam(value = "smokingAllowed", required = false)
                                              String smoking,
                                      @RequestParam(value = "petsAllowed", required = false)
                                              String pets,
                                      @RequestParam(value = "luggageAllowed", required = false)
                                              String luggage,
                                      @RequestParam(value = "airConditioned", required = false)
                                              String airConditioned)

    {

        return Optional
                .ofNullable(dtoMapper.tripToDtoList(tripService.getTrips(pageNumber, pageSize, tripStatus, origin, destination,
                        earliestDepartureTime, latestDepartureTime, availablePlaces, smoking, pets, luggage,airConditioned)))
                .map(tripDtoResponse -> ResponseEntity.ok().body(tripDtoResponse))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<?> createTrip(@Valid @RequestBody final TripDtoRequest tripDtoRequest,
                                        final Authentication authentication) {

        return Optional
                .ofNullable(tripService.createTrip(dtoMapper.dtoToObject(tripDtoRequest), userService.getUser(
                        authentication.getName())))
                .map(k -> ResponseEntity.status(HttpStatus.CREATED).build())
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping
    public ResponseEntity<?> updateTrip(@Valid @RequestBody final TripDtoEdit tripDtoEdit,final Authentication authentication) {
        return Optional
                .ofNullable(tripService.updateTrip(dtoMapper.dtoToObject(tripDtoEdit),userService.getUser(authentication.getName())))
                .map(tripResponseDto -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping(value = "/{tripID}")
    public ResponseEntity<?> changeTripStatus(@PathVariable final String tripID,
                                              final Authentication authentication,
                                              @RequestParam(value = "status") TripStatus tripStatus) {


        return Optional
                .ofNullable(tripService.changeTripStatus(tripID,
                        userService.getUser(authentication.getName()), tripStatus))
                .map(k -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<TripDtoResponse> getTrip(@PathVariable String id) {

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
                .ofNullable(tripService.addPassenger(id, userService.getUser(
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
                .ofNullable(tripService.changePassengerStatus(tripId, userService.getUser(
                        authentication.getName()), passengerId, passengerStatus))
                .map(k -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping(value = "/{tripId}/delete")
    public ResponseEntity<?> deleteTrip(@PathVariable final String tripId,
                                        final Authentication authentication) {
        return Optional
                .ofNullable(tripService.deleteTrip(tripId, userService.getUser(
                        authentication.getName())))
                .map(k -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/{tripId}/comments")
    public ResponseEntity<?> createComment(@PathVariable final String tripId,
                                           final Authentication authentication,
                                           @RequestParam(value = "comment") final String message) {
        return Optional
                .ofNullable(commentService.createComment(tripId, userService.getUser(
                        authentication.getName()), message))
                .map(commentDto -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping(value = "/{tripId}/comments")
    public ResponseEntity<?>getComments(@PathVariable final String tripId){
        return Optional
                .ofNullable(commentService.getComments(tripId))
                .map(value -> ResponseEntity.ok().body(value))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
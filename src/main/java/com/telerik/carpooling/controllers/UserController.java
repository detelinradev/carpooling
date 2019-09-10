package com.telerik.carpooling.controllers;

import com.telerik.carpooling.models.dtos.*;
import com.telerik.carpooling.services.services.contracts.FeedbackService;
import com.telerik.carpooling.services.services.contracts.RatingService;
import com.telerik.carpooling.services.services.contracts.UserService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@CrossOrigin(maxAge = 3600)
@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping(value = "users")
public class UserController {

    private final UserService userService;
    private final RatingService ratingService;
    private final FeedbackService feedbackService;

    @PostMapping(value = "/register")
    public ResponseEntity<UserDtoResponse> save(@Valid @RequestBody final UserDtoRequest userDtoRequest) {

        return ResponseEntity.ok().body(userService.save(userDtoRequest));
    }

    @PostMapping(value = "/rate/{tripId}/user/{username}")
    public ResponseEntity<Void> rateUser(@PathVariable final Long tripId,
                                         @PathVariable final String username,
                                         final Authentication authentication,
                                         @RequestBody final Integer rating) throws NotFoundException {

        ratingService.rateUser(tripId, authentication.getName(), username, rating);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/feedback/{tripId}/user/{username}")
    public ResponseEntity<Void> leaveFeedback(@PathVariable final Long tripId,
                                              @PathVariable final String username,
                                              final Authentication authentication,
                                              @RequestBody final String feedback) throws NotFoundException {

        feedbackService.leaveFeedback(tripId, authentication.getName(), username, feedback);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<UserDtoResponse>> getUsers(@RequestParam(value = "_end", required = false) final Integer pageNumber,
                                                          @RequestParam(value = "_start", required = false) final Integer pageSize,
                                                          @RequestParam(value = "username", required = false) final String username,
                                                          @RequestParam(value = "firstName", required = false) final String firstName,
                                                          @RequestParam(value = "lastName", required = false) final String lastName,
                                                          @RequestParam(value = "email", required = false) final String email,
                                                          @RequestParam(value = "phone", required = false) final String phone) {

        return ResponseEntity.ok().body(userService.getUsers(pageNumber, pageSize, username, firstName, lastName, email,
                phone));
    }

    @GetMapping(value = "/top-rated-users")
    public ResponseEntity<List<UserDtoResponse>> getTopRatedUsers(@RequestParam(value = "isPassenger") final Boolean isPassenger) {

        return ResponseEntity.ok().body(userService.getTopRatedUsers(isPassenger));

    }

    @GetMapping(value = "myTrips")
    public ResponseEntity<List<TripDtoResponse>> getMyTrips(final Authentication authentication) {

        return ResponseEntity.ok().body(userService.getUserOwnTrips(authentication.getName()));
    }

    @GetMapping(value = "/{username}")
    public ResponseEntity<UserDtoResponse> getUser(@PathVariable final String username, final Authentication authentication) {

        return ResponseEntity.ok().body(userService.getUser(username, authentication.getName()));
    }

    @GetMapping(value = "/{username}/feedback")
    public ResponseEntity<Set<FeedbackDtoResponse>> getFeedback(@PathVariable final String username) {

        return ResponseEntity.ok().body(feedbackService.getFeedback(username));
    }

    @PutMapping
    public ResponseEntity<UserDtoResponse> editUser(@Valid @RequestBody final UserDtoEdit userDtoEdit, final Authentication authentication) {

        return ResponseEntity.ok().body(userService.updateUser(userDtoEdit, authentication.getName()));
    }

    @DeleteMapping(value = "/{username}/delete")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> deleteUser(@PathVariable final String username) {

        userService.deleteUser(username);
        return ResponseEntity.ok().build();
    }
}

package com.telerik.carpooling.controller;

import com.telerik.carpooling.model.dto.FeedbackDtoResponse;
import com.telerik.carpooling.model.dto.UserDtoEdit;
import com.telerik.carpooling.model.dto.UserDtoRequest;
import com.telerik.carpooling.model.dto.UserDtoResponse;
import com.telerik.carpooling.service.service.contract.FeedbackService;
import com.telerik.carpooling.service.service.contract.RatingService;
import com.telerik.carpooling.service.service.contract.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@CrossOrigin(maxAge = 3600)
@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping(value = "users")
@Validated
public class UserController {

    private final UserService userService;
    private final RatingService ratingService;
    private final FeedbackService feedbackService;

    @PostMapping(value = "/register")
    public ResponseEntity<UserDtoResponse> create(@Valid @RequestBody final UserDtoRequest userDtoRequest) {

        return ResponseEntity.ok().body(userService.createUser(userDtoRequest));
    }

    @PostMapping(value = "/rate/{tripId}/user/{username}")
    public ResponseEntity<Void> rateUser(
            @PathVariable @NotNull final Long tripId,
            @PathVariable @NotNull final String username,
            @RequestBody @NotNull
            @Min(value = 1, message = "Rating value should be at least one")
            @Max(value = 5, message = "Rating value should be at most five") final Integer rating) {

        final String loggedUserUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        ratingService.createRating(tripId, loggedUserUsername, username, rating);
        ratingService.setUserRating(tripId, username, rating);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/feedback/{tripId}/user/{username}")
    public ResponseEntity<Void> leaveFeedback(@PathVariable @NotNull final Long tripId,
                                              @PathVariable @NotNull final String username,
                                              @RequestBody @NotNull final String feedback) {

        final String loggedUserUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        feedbackService.leaveFeedback(tripId, loggedUserUsername, username, feedback);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<UserDtoResponse>> getUsers
            (@RequestParam(value = "_end", required = false, defaultValue = "0") final Integer pageNumber,
             @RequestParam(value = "_start", required = false, defaultValue = "10") final Integer pageSize,
             @RequestParam(value = "username", required = false) final String username,
             @RequestParam(value = "firstName", required = false) final String firstName,
             @RequestParam(value = "lastName", required = false) final String lastName,
             @RequestParam(value = "email", required = false) final String email,
             @RequestParam(value = "phone", required = false) final String phone) {

        return ResponseEntity.ok().body(userService.getUsers(pageNumber, pageSize, username, firstName, lastName, email,
                phone));
    }

    @GetMapping(value = "/top-rated-users")
    public ResponseEntity<List<UserDtoResponse>> getTopRatedUsers(
            @RequestParam(value = "isPassenger")
            @NotNull(message = "isPassenger parameter should not be null") final Boolean isPassenger) {

        return ResponseEntity.ok().body(userService.getTopRatedUsers(isPassenger));

    }


    @GetMapping(value = "/{username}")
    public ResponseEntity<UserDtoResponse> getUser(@PathVariable @NotNull final String username) {

        final String loggedUserUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.ok().body(userService.getUser(username, loggedUserUsername));
    }

    @GetMapping(value = "/{username}/feedback")
    public ResponseEntity<Set<FeedbackDtoResponse>> getFeedback(@PathVariable @NotNull final String username) {

        return ResponseEntity.ok().body(feedbackService.getFeedback(username));
    }

    @PutMapping
    public ResponseEntity<UserDtoResponse> editUser(@Valid @RequestBody final UserDtoEdit userDtoEdit) {

        final String loggedUserUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.ok().body(userService.updateUser(userDtoEdit, loggedUserUsername));
    }

    @DeleteMapping(value = "/{username}/delete")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> deleteUser(@PathVariable @NotNull final String username) {

        userService.deleteUser(username);
        return ResponseEntity.ok().build();
    }
}

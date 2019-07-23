package com.telerik.carpooling.controllers;

import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.TripDtoResponse;
import com.telerik.carpooling.models.dtos.UserDtoRequest;
import com.telerik.carpooling.models.dtos.UserDtoResponse;
import com.telerik.carpooling.repositories.UserRepository;
import com.telerik.carpooling.security.AuthenticationService;
import com.telerik.carpooling.services.services.contracts.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@CrossOrigin(maxAge = 3600)
@RestController
@RequiredArgsConstructor
@Log4j2
public class UserController {

    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @GetMapping(value = "api/me")
    public ResponseEntity<User> getUserOwnInfo(final HttpServletRequest req) {
        log.error("logger happened here");
        return Optional
                .ofNullable(userRepository.findFirstByUsername(
                        authenticationService.getUsername(req)))
                .map(user -> ResponseEntity.ok().body(user))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PatchMapping(value = "api/me/update-password")
    public ResponseEntity<User> updateUserOwnInfo(@RequestParam final String password, final HttpServletRequest req) {

        return Optional
                .ofNullable(userService.updateCurrentUserPassword(password, userRepository.findFirstByUsername(
                        authenticationService.getUsername(req))))
                .map(user -> ResponseEntity.ok().body(user))
                .orElseGet(() -> ResponseEntity.badRequest().build());

    }

    @PatchMapping(value = "api/me/update-email")
    public ResponseEntity<User> updateUserOwnEmail(@RequestParam final String email, final HttpServletRequest req) {
        return Optional
                .ofNullable(userService.updateCurrentUserEmail(email, userRepository.findFirstByUsername(
                        authenticationService.getUsername(req))))
                .map(user -> ResponseEntity.ok().body(user))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping(value = "api/sign-up")
    public ResponseEntity<User> save(@Valid @RequestBody final UserDtoRequest user) {
        return new ResponseEntity<>(userService.save(user), HttpStatus.OK);
    }

    @PutMapping(value = "/rate")
    public ResponseEntity<UserDtoResponse> rateUser(@Valid @RequestBody final TripDtoResponse trip,
                                                    final HttpServletRequest req,
                                                    @RequestParam(value = "userRole") String userRole,
                                                    @RequestParam(value = "ratedUserID") int ratedUserID,
                                                    @RequestParam(value = "ratedUserRole") String ratedUserROle,
                                                    @RequestParam(value = "rating") int rating) {

        return Optional
                .ofNullable(userService.rateUser(trip,userRepository.findFirstByUsername(
                        authenticationService.getUsername(req)), userRole, ratedUserID, ratedUserROle, rating))
                .map(tripDtoResponse -> ResponseEntity.ok().body(tripDtoResponse))
                .orElseGet(() -> ResponseEntity.badRequest().build());
        // ResponseEntity.of(tripService.rateUser(trip, userRole, ratedUserID, ratedUserROle, rating));
    }

    @PutMapping(value = "/feedback")
    public ResponseEntity<UserDtoResponse> leaveFeedback(@Valid @RequestBody final TripDtoResponse trip,
                                                    final HttpServletRequest req,
                                                    @RequestParam(value = "userRole") String userRole,
                                                    @RequestParam(value = "userToGetFeedbackId") int ratedUserID,
                                                    @RequestParam(value = "userToGetFeedbackRole") String ratedUserROle,
                                                    @RequestParam(value = "feedback") String feedback) {

        return Optional
                .ofNullable(userService.leaveFeedback(trip,userRepository.findFirstByUsername(
                        authenticationService.getUsername(req)),userRole, ratedUserID, ratedUserROle, feedback))
                .map(tripDtoResponse -> ResponseEntity.ok().body(tripDtoResponse))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

}

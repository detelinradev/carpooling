package com.telerik.carpooling.controllers;

import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.UserDtoRequest;
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

}

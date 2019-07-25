package com.telerik.carpooling.controllers;

import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.TripDtoResponse;
import com.telerik.carpooling.models.dtos.UserDtoRequest;
import com.telerik.carpooling.repositories.UserRepository;
import com.telerik.carpooling.services.services.contracts.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@CrossOrigin(maxAge = 3600)
@RestController
@RequiredArgsConstructor
@Log4j2
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping(value = "/me")
    public ResponseEntity<User> getUserOwnInfo(Authentication authentication) {
        return Optional
                .ofNullable(userRepository.findFirstByUsername(
                        authentication.getName()))
                .map(user -> ResponseEntity.ok().body(user))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PatchMapping(value = "/me/update-password")
    public ResponseEntity<User> updateUserOwnInfo(@RequestParam final String password, final Authentication authentication) {

        return Optional
                .ofNullable(userService.updateCurrentUserPassword(password, userRepository.findFirstByUsername(
                        authentication.getName())))
                .map(user -> ResponseEntity.ok().body(user))
                .orElseGet(() -> ResponseEntity.badRequest().build());

    }

    @PatchMapping(value = "/me/update-email")
    public ResponseEntity<User> updateUserOwnEmail(@RequestParam final String email, final Authentication authentication) {
        return Optional
                .ofNullable(userService.updateCurrentUserEmail(email, userRepository.findFirstByUsername(
                        authentication.getName())))
                .map(user -> ResponseEntity.ok().body(user))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping(value = "/sign-up")
    public ResponseEntity<User> save(@Valid @RequestBody final UserDtoRequest user) {
        return new ResponseEntity<>(userService.save(user), HttpStatus.OK);
    }
}

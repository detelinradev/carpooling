package com.telerik.carpooling.controllers;

import com.telerik.carpooling.models.dtos.*;
import com.telerik.carpooling.repositories.UserRepository;
import com.telerik.carpooling.security.AuthenticationService;
import com.telerik.carpooling.services.services.contracts.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@CrossOrigin(maxAge = 3600)
@RestController
@RequiredArgsConstructor
public class CommentController {


    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final CommentService commentService;

    @PostMapping(value = "/comment")
    public ResponseEntity<CommentDtoResponse> createComment(@Valid @RequestBody final TripDtoResponse trip,
                                                            final HttpServletRequest req,
                                                            @Valid@RequestParam(value = "message") final String message){

        return Optional
                .ofNullable(commentService.createComment( trip, userRepository.findFirstByUsername(
                        authenticationService.getUsername(req)), message))
                .map(commentDto -> ResponseEntity.ok().body(commentDto))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

}

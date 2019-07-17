package com.telerik.carpooling.controllers;

import com.telerik.carpooling.models.dtos.DtoMapper;
import com.telerik.carpooling.models.dtos.TripDto;
import com.telerik.carpooling.repositories.UserRepository;
import com.telerik.carpooling.security.AuthenticationService;
import com.telerik.carpooling.services.TripService;
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
    public ResponseEntity<TripDto>createTrip(@Valid@RequestBody final TripDto trip, final HttpServletRequest req){

        return Optional
                .ofNullable(tripService.createTrip(dtoMapper.dtoToObject(trip), userRepository.findFirstByUsername(
                        authenticationService.getUsername(req))))
                .map(tripDto -> ResponseEntity.ok().body(tripDto))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping(value = "/update")
    public ResponseEntity<TripDto>updateTrip(@Valid@RequestBody final TripDto trip, final HttpServletRequest req){

        return Optional
                .ofNullable(tripService.updateTrip(dtoMapper.dtoToObject(trip), userRepository.findFirstByUsername(
                        authenticationService.getUsername(req))))
                .map(tripDto -> ResponseEntity.ok().body(tripDto))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }


}

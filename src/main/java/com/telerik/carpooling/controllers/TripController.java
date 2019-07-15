package com.telerik.carpooling.controllers;

import com.telerik.carpooling.models.Trip;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/trip")
public class TripController {

//    @PostMapping(value = "/create")
//    public ResponseEntity<Trip>createTrip(@Valid @RequestBody final Trip trip){
//        return new ResponseEntity<>(tripService.create(trip), HttpStatus.OK);
//    }
}

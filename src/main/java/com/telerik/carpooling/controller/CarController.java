package com.telerik.carpooling.controller;

import com.telerik.carpooling.exception.MyNotFoundException;
import com.telerik.carpooling.model.dto.CarDtoEdit;
import com.telerik.carpooling.model.dto.CarDtoRequest;
import com.telerik.carpooling.model.dto.CarDtoResponse;
import com.telerik.carpooling.service.service.contract.CarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@CrossOrigin(maxAge = 3600)
@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("car")
@Validated
public class CarController {

    private final CarService carService;

    @PostMapping
    public ResponseEntity<CarDtoResponse> createCar(@Valid @RequestBody final CarDtoRequest car){

        String loggedUserUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.ok().body(carService.createCar(car, loggedUserUsername));
    }

    @GetMapping(value = "/{username}")
    public ResponseEntity<CarDtoResponse> getCar(@PathVariable @NotNull final String username)
            throws MyNotFoundException {

        return ResponseEntity.ok().body(carService.getCar(username));
    }

    @PutMapping
    public ResponseEntity<CarDtoResponse> updateCar(@Valid @RequestBody final CarDtoEdit car) {

        String loggedUserUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.ok().body(carService.updateCar(car, loggedUserUsername));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable @NotNull final Long id) {

        String loggedUserUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        carService.deleteCar(id, loggedUserUsername);
        return ResponseEntity.ok().build();
    }

}

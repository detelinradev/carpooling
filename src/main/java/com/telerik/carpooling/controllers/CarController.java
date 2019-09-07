package com.telerik.carpooling.controllers;

import com.telerik.carpooling.models.dtos.CarDtoEdit;
import com.telerik.carpooling.models.dtos.CarDtoRequest;
import com.telerik.carpooling.models.dtos.CarDtoResponse;
import com.telerik.carpooling.services.services.contracts.CarService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(maxAge = 3600)
@RestController
@RequiredArgsConstructor
@Log4j2
public class CarController {

    private final CarService carService;

    @PostMapping(value = "/car")
    public ResponseEntity<CarDtoResponse> createCar(@Valid @RequestBody final CarDtoRequest car,
                                                    Authentication authentication) {

        return ResponseEntity.ok().body(carService.createCar(car, authentication.getName()));
    }

    @GetMapping(value = "/{username}")
    public ResponseEntity<CarDtoResponse> getCar(@PathVariable final String username)
            throws NotFoundException {

        return ResponseEntity.ok().body(carService.getCar(username));
    }

    @PutMapping(value = "/car")
    public ResponseEntity<CarDtoResponse> updateCar(@Valid @RequestBody final CarDtoEdit car,
                                                    Authentication authentication) {

        return ResponseEntity.ok().body(carService.updateCar(car, authentication.getName()));
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable final Long id, final Authentication authentication)
            throws NotFoundException {

        carService.deleteCar(id,authentication.getName());
        return ResponseEntity.ok().build();
    }

}

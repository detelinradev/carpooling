package com.telerik.carpooling.controller;

import com.telerik.carpooling.model.dto.CarDtoEdit;
import com.telerik.carpooling.model.dto.CarDtoRequest;
import com.telerik.carpooling.model.dto.CarDtoResponse;
import com.telerik.carpooling.service.service.contract.CarService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    public ResponseEntity<CarDtoResponse> createCar(@Valid @RequestBody final CarDtoRequest car,
                                                    Authentication authentication) {

        return ResponseEntity.ok().body(carService.createCar(car, authentication.getName()));
    }

    @GetMapping(value = "/{username}")
    public ResponseEntity<CarDtoResponse> getCar(@PathVariable @NotNull final String username)
            throws NotFoundException {

        return ResponseEntity.ok().body(carService.getCar(username));
    }

    @PutMapping
    public ResponseEntity<CarDtoResponse> updateCar(@Valid @RequestBody final CarDtoEdit car,
                                                    Authentication authentication) {

        return ResponseEntity.ok().body(carService.updateCar(car, authentication.getName()));
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable @NotNull final Long id, final Authentication authentication)
            throws NotFoundException {

        carService.deleteCar(id,authentication.getName());
        return ResponseEntity.ok().build();
    }

}

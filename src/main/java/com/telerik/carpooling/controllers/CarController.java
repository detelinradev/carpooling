package com.telerik.carpooling.controllers;

import com.telerik.carpooling.models.Car;
import com.telerik.carpooling.models.dtos.CarDtoRequest;
import com.telerik.carpooling.models.dtos.CarDtoResponse;
import com.telerik.carpooling.models.dtos.dtos.mapper.DtoMapper;
import com.telerik.carpooling.repositories.CarRepository;
import com.telerik.carpooling.repositories.UserRepository;
import com.telerik.carpooling.services.services.contracts.CarService;
import com.telerik.carpooling.services.services.contracts.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@CrossOrigin(maxAge = 3600)
@RestController
@RequiredArgsConstructor
@Log4j2
public class CarController {

    private final UserService userService;
    private final CarService carService;

    @PostMapping(value = "/car")
    public ResponseEntity<CarDtoResponse> createCar(@Valid @RequestBody final CarDtoRequest car,
                                                    final Authentication authentication){

        return Optional
                .ofNullable(carService.createCar(car, userService.getUser(
                        authentication.getName())))
                .map(carDto -> ResponseEntity.ok().body(carDto))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping(value = "/car")
    public ResponseEntity<CarDtoResponse> updateCar(@Valid @RequestBody final CarDtoResponse car,
                                                    final Authentication authentication){
        return Optional
                .ofNullable(carService.updateCar(car ,userService.getUser(
                        authentication.getName())))
                .map(carDto -> ResponseEntity.ok().body(carDto))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping(value = "/car/{id}")
    public ResponseEntity<CarDtoResponse> getCar(@PathVariable final long id){
        return Optional
                .ofNullable(carService.getCar(id))
                .map(car -> ResponseEntity.ok().body(car))
                .orElseGet(() -> ResponseEntity.ok().build());
    }

    @GetMapping(value = "/carMe")
    public ResponseEntity<CarDtoResponse> getCarMe(final Authentication authentication){
        return Optional
                .ofNullable(carService.getCarMe(userService.getUser(
                        authentication.getName())))
                .map(car -> ResponseEntity.ok().body(car))
                .orElseGet(() -> ResponseEntity.ok().build());
    }
}

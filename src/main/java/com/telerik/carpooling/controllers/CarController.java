package com.telerik.carpooling.controllers;

import com.telerik.carpooling.models.Car;
import com.telerik.carpooling.models.dtos.CarDtoRequest;
import com.telerik.carpooling.models.dtos.CarDtoResponse;
import com.telerik.carpooling.models.dtos.dtos.mapper.DtoMapper;
import com.telerik.carpooling.repositories.CarRepository;
import com.telerik.carpooling.repositories.UserRepository;
import com.telerik.carpooling.security.AuthenticationService;
import com.telerik.carpooling.services.services.contracts.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@CrossOrigin(maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/car")
public class CarController {

    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final CarService carService;

    @PostMapping(value = "/car")
    public ResponseEntity<CarDtoResponse> createCar(@Valid @RequestBody final CarDtoRequest car, final HttpServletRequest req){


        return Optional
                .ofNullable(carService.createCar(car, userRepository.findFirstByUsername(
                        authenticationService.getUsername(req))))
                .map(carDto -> ResponseEntity.ok().body(carDto))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping(value = "/car")
    public ResponseEntity<CarDtoResponse> updateCar(@Valid @RequestBody final CarDtoResponse car, final HttpServletRequest req){

        return Optional
                .ofNullable(carService.updateCar(car ,userRepository.findFirstByUsername(
                        authenticationService.getUsername(req))))
                .map(carDto -> ResponseEntity.ok().body(carDto))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping(value = "/car")
    public ResponseEntity<Car> getCar(final HttpServletRequest req){

        return Optional
                .ofNullable(userRepository.findFirstByUsername(
                        authenticationService.getUsername(req)).getCar())
                .map(car -> ResponseEntity.ok().body(car))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
}

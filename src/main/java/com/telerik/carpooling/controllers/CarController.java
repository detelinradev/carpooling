package com.telerik.carpooling.controllers;

import com.telerik.carpooling.models.Car;
import com.telerik.carpooling.models.dtos.CarDto;
import com.telerik.carpooling.repositories.UserRepository;
import com.telerik.carpooling.security.AuthenticationService;
import com.telerik.carpooling.services.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@CrossOrigin(maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/car")
public class CarController {

    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final CarService carService;

    @PutMapping(value = "/car")
    public ResponseEntity<Car>createCar(@Valid @RequestBody final CarDto car, final HttpServletRequest req){

        return Optional
                .ofNullable(carService.createCar(car, userRepository.findFirstByUsername(
                        authenticationService.getUsername(req))))
                .map(carDto -> ResponseEntity.ok().body(carDto))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
}

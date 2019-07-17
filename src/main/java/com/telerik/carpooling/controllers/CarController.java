package com.telerik.carpooling.controllers;

import com.telerik.carpooling.models.Car;
import com.telerik.carpooling.models.dtos.CarDto;
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
    private final CarRepository carRepository;
    private final AuthenticationService authenticationService;
    private final CarService carService;
    private final DtoMapper dtoMapper;

    @PostMapping(value = "/car")
    public ResponseEntity<CarDto> createCar(@Valid @RequestBody final CarDto car, final HttpServletRequest req){

        if(carRepository.count()!=0){
            return null;
        }

        return Optional
                .ofNullable(carService.createCar(dtoMapper.dtoToObject(car), userRepository.findFirstByUsername(
                        authenticationService.getUsername(req))))
                .map(carDto -> ResponseEntity.ok().body(carDto))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping(value = "/car")
    public ResponseEntity<CarDto> updateCar(@Valid @RequestBody final CarDto car, final HttpServletRequest req){

        return Optional
                .ofNullable(carService.updateCar(dtoMapper.dtoToObject(car), userRepository.findFirstByUsername(
                        authenticationService.getUsername(req))))
                .map(tripDto -> ResponseEntity.ok().body(tripDto))
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

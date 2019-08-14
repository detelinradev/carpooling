package com.telerik.carpooling.controllers;

import com.telerik.carpooling.models.Car;
import com.telerik.carpooling.models.dtos.CarDtoRequest;
import com.telerik.carpooling.models.dtos.CarDtoResponse;
import com.telerik.carpooling.models.dtos.dtos.mapper.DtoMapper;
import com.telerik.carpooling.repositories.CarRepository;
import com.telerik.carpooling.repositories.UserRepository;
import com.telerik.carpooling.services.services.contracts.CarService;
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

    private final UserRepository userRepository;
    private final CarService carService;
    private final DtoMapper dtoMapper;

    @PostMapping(value = "/car")
    public ResponseEntity<CarDtoResponse> createCar(@Valid @RequestBody final Car car,
                                                    final Authentication authentication){

        return Optional
                .ofNullable(dtoMapper.objectToDto(carService.createCar(car, userRepository.findFirstByUsername(
                        authentication.getName()))))
                .map(carDto -> ResponseEntity.ok().body(carDto))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping(value = "/car")
    public ResponseEntity<CarDtoResponse> updateCar(@Valid @RequestBody final Car car,
                                                    final Authentication authentication){
        return Optional
                .ofNullable(dtoMapper.objectToDto(carService.updateCar(car ,userRepository.findFirstByUsername(
                        authentication.getName()))))
                .map(carDto -> ResponseEntity.ok().body(carDto))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping(value = "/car/{id}")
    public ResponseEntity<CarDtoResponse> getCar(@PathVariable final long id){
        return Optional
                .ofNullable(dtoMapper.objectToDto(carService.getCar(id)))
                .map(car -> ResponseEntity.ok().body(car))
                .orElseGet(() -> ResponseEntity.ok().build());
    }

    @GetMapping(value = "/carMe")
    public ResponseEntity<CarDtoResponse> getCarMeMe(final Authentication authentication){
        return Optional
                .ofNullable(dtoMapper.objectToDto(carService.getCarMe(userRepository.findFirstByUsername(
                        authentication.getName()))))
                .map(car -> ResponseEntity.ok().body(car))
                .orElseGet(() -> ResponseEntity.ok().build());
    }
}

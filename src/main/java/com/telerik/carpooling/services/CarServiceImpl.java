package com.telerik.carpooling.services;

import com.telerik.carpooling.models.Car;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.CarDtoRequest;
import com.telerik.carpooling.models.dtos.CarDtoResponse;
import com.telerik.carpooling.models.dtos.dtos.mapper.DtoMapper;
import com.telerik.carpooling.repositories.CarRepository;
import com.telerik.carpooling.repositories.UserRepository;
import com.telerik.carpooling.services.services.contracts.CarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Log4j2
public class CarServiceImpl implements CarService {


    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final DtoMapper dtoMapper;


    @Override
    public CarDtoResponse getCarMe(User user) {
        try {
            return dtoMapper.objectToDto(user.getCar());
        }catch (NullPointerException e){
            log.error("User does not have a car", e);
        }
        return null;
    }

    @Override
    public CarDtoResponse getCar(long  id) {
        Optional<User> user =userRepository.findById(id);
        try {
            return user.map(value -> dtoMapper.objectToDto(value.getCar())).orElse(null);
        }catch (NullPointerException e){
            log.error("User does not have a car", e);
        }
        return null;

    }

    @Override
    public CarDtoResponse createCar(CarDtoRequest carDtoRequest, User owner) {
        Car car = dtoMapper.dtoToObject(carDtoRequest);
        car.setOwner(owner);
        return dtoMapper.objectToDto(carRepository.save(car));
    }

    @Override
    public CarDtoResponse updateCar(CarDtoResponse carDtoResponse, User owner) {
        Car car = dtoMapper.dtoToObject(carDtoResponse);
        if(car != null) {
            car.setModelId(owner.getCar().getModelId());
            car.setOwner(owner);
            return dtoMapper.objectToDto(carRepository.save(car));
        }else return null;
    }

}

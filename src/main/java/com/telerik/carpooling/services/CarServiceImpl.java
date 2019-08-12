package com.telerik.carpooling.services;

import com.telerik.carpooling.models.Car;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.CarDtoRequest;
import com.telerik.carpooling.models.dtos.CarDtoResponse;
import com.telerik.carpooling.models.dtos.dtos.mapper.DtoMapper;
import com.telerik.carpooling.repositories.CarRepository;
import com.telerik.carpooling.services.services.contracts.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {


    private final CarRepository carRepository;
    private final DtoMapper dtoMapper;


    @Override
    public Car getCar(User user) {
        return user.getCar();
    }

    @Override
    public Car createCar(Car car, User owner) {
        car.setOwner(owner);
        return carRepository.save(car);
    }

    @Override
    public Car updateCar(Car car, User owner) {
        car.setModelId(owner.getCar().getModelId());
        car.setOwner(owner);
        return carRepository.save(car);
    }

}

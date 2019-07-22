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
    public CarDtoResponse createCar(CarDtoRequest carDtoRequest, User owner) {

        Car car = dtoMapper.dtoToObject(carDtoRequest);
        car.setOwner(owner);

        return dtoMapper.objectToDto(carRepository.save(car));
    }

    @Override
    public CarDtoResponse updateCar(CarDtoResponse carDtoResponse, User owner) {
        Car car = dtoMapper.dtoToObject(carDtoResponse);
        car.setId(owner.getCar().getId());
        car.setOwner(owner);
        return dtoMapper.objectToDto(carRepository.save(car));
    }

}

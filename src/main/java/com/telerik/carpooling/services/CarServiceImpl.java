package com.telerik.carpooling.services;

import com.telerik.carpooling.models.Car;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.CarDto;
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
    public CarDto createCar(final Car car, final User owner) {
            car.setOwner(owner);
            carRepository.save(car);
            return dtoMapper.objectToDto(car);

    }

    @Override
    public CarDto updateCar(final Car car, final User owner) {
        owner.setCar(car);
        car.setOwner(owner);
        carRepository.save(car);
        return dtoMapper.objectToDto(car);
    }

}

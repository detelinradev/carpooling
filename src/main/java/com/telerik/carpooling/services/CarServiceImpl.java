package com.telerik.carpooling.services;

import com.telerik.carpooling.exceptions.MyFileNotFoundException;
import com.telerik.carpooling.models.Car;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.CarDto;
import com.telerik.carpooling.models.dtos.DtoMapper;
import com.telerik.carpooling.repositories.CarRepository;
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
    public CarDto updateCar(Car car, User owner) {
        car.setOwner(owner);
        carRepository.save(car);
        return dtoMapper.objectToDto(car);
    }

    @Override
    public CarDto deleteCar(Car car, User owner) {
        if(carRepository.count()!=0) {
            carRepository.delete(car);
            owner.setCar(null);
            return dtoMapper.objectToDto(car);
        }
        return null;
    }

    public CarDto getCarById(final int carId) {
        return dtoMapper.objectToDto(carRepository.findById(carId)
                .orElseThrow(() -> new MyFileNotFoundException("Car not found with id " + carId)));

    }

}

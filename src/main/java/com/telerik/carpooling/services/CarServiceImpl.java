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
           CarDto newCar = dtoMapper.objectToDto(car);

//        Car newCar = new Car();
//        newCar.setBrand(car.getBrand());
//        newCar.setModel(car.getModel());
//        newCar.setColor(car.getColor());
//        newCar.setFirstRegistration(car.getFirstRegistration());
//        newCar.setTotalSeats(car.getTotalSeats());
//        newCar.setAirConditioned(false);
//        newCar.setLuggageAllowed(false);
//        newCar.setPetsAllowed(false);
//        newCar.setSmokingAllowed(false);
//        newCar.setOwner(owner);
//        return carRepository.save(newCar);
        return null;
    }

}

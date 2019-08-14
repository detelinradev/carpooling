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
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {


    private final CarRepository carRepository;
    private final UserRepository userRepository;


    @Override
    public Car getCarMe(User user) {
        return user.getCar();
    }

    @Override
    public Car getCar(long  id) {
        Optional<User> user =userRepository.findById(id);
        Car car;
        if(user.isPresent()) {
            car = user.get().getCar();
            return car;
        }
        return null;
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

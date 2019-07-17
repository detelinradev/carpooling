package com.telerik.carpooling.services;

import com.telerik.carpooling.models.Car;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.CarDto;

public interface CarService {

    CarDto createCar(Car car, User owner);

    CarDto updateCar(Car car, User owner);

    CarDto deleteCar(Car car, User owner);

    CarDto getCarById(int carId);
}

package com.telerik.carpooling.services.services.contracts;

import com.telerik.carpooling.models.Car;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.CarDtoRequest;
import com.telerik.carpooling.models.dtos.CarDtoResponse;

public interface CarService {

    CarDtoResponse getCar(User user);

    CarDtoResponse createCar(CarDtoRequest car, User owner);

    CarDtoResponse updateCar(CarDtoResponse car, User owner);


}

package com.telerik.carpooling.services.services.contracts;

import com.telerik.carpooling.models.Car;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.CarDtoRequest;
import com.telerik.carpooling.models.dtos.CarDtoResponse;

public interface CarService {

    Car getCar(long id);

    Car createCar(Car car, User owner);

    Car updateCar(Car car, User owner);


    Car getCarMe(User firstByUsername);
}

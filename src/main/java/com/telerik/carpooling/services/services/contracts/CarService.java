package com.telerik.carpooling.services.services.contracts;

import com.telerik.carpooling.models.Car;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.CarDtoRequest;
import com.telerik.carpooling.models.dtos.CarDtoResponse;

public interface CarService {

    CarDtoResponse getCar(long id);

    CarDtoRequest createCar(Car car, User owner);

    CarDtoResponse updateCar(Car car, User owner);


    CarDtoResponse getCarMe(User firstByUsername);
}

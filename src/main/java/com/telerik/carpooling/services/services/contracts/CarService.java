package com.telerik.carpooling.services.services.contracts;

import com.telerik.carpooling.models.dtos.CarDtoEdit;
import com.telerik.carpooling.models.dtos.CarDtoRequest;
import com.telerik.carpooling.models.dtos.CarDtoResponse;
import javassist.NotFoundException;

public interface CarService {


    CarDtoResponse createCar(CarDtoRequest car, String loggedUserUsername);

    CarDtoResponse updateCar(CarDtoEdit car, String loggedUserUsername);

    void deleteCar(Long id, String username) throws NotFoundException;

    CarDtoResponse getCar(String username) throws NotFoundException;
}

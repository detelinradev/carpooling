package com.telerik.carpooling.service.service.contract;

import com.telerik.carpooling.model.dto.CarDtoEdit;
import com.telerik.carpooling.model.dto.CarDtoRequest;
import com.telerik.carpooling.model.dto.CarDtoResponse;
import javassist.NotFoundException;

public interface CarService {


    CarDtoResponse createCar(CarDtoRequest car, String loggedUserUsername);

    CarDtoResponse updateCar(CarDtoEdit car, String loggedUserUsername);

    void deleteCar(Long id, String username) throws NotFoundException;

    CarDtoResponse getCar(String username) throws NotFoundException;
}

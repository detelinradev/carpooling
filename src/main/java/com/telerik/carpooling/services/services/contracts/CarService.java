package com.telerik.carpooling.services.services.contracts;

import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.CarDtoEdit;
import com.telerik.carpooling.models.dtos.CarDtoRequest;
import com.telerik.carpooling.models.dtos.CarDtoResponse;
import javassist.NotFoundException;
import org.springframework.security.core.Authentication;

public interface CarService {


    CarDtoResponse createCar(CarDtoRequest car, String loggedUserUsername);

    CarDtoResponse updateCar(CarDtoEdit car, String loggedUserUsername);

    void deleteCar(Long id, String username) throws NotFoundException;

    CarDtoResponse getCar(String username) throws NotFoundException;
}

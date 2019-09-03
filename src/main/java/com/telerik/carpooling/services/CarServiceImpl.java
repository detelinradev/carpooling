package com.telerik.carpooling.services;

import com.telerik.carpooling.models.Car;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.CarDtoEdit;
import com.telerik.carpooling.models.dtos.CarDtoRequest;
import com.telerik.carpooling.models.dtos.CarDtoResponse;
import com.telerik.carpooling.models.dtos.dtos.mapper.DtoMapper;
import com.telerik.carpooling.repositories.CarRepository;
import com.telerik.carpooling.repositories.UserRepository;
import com.telerik.carpooling.services.services.contracts.CarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Log4j2
public class CarServiceImpl implements CarService {


    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final DtoMapper dtoMapper;

    @Override
    public CarDtoResponse createCar(CarDtoRequest carDtoRequest, String username) {
        Car car = dtoMapper.dtoToObject(carDtoRequest);
        User owner = findUserByUsername(username);
        car.setOwner(owner);
        car.setIsDeleted(false);
        return dtoMapper.objectToDto(carRepository.save(car));
    }

    @Override
    public CarDtoResponse updateCar(CarDtoEdit carDtoEdit, String loggedUserUsername) {

        Car car = dtoMapper.dtoToObject(carDtoEdit);
        User owner = findUserByUsername(loggedUserUsername);
        if(car.equals(owner.getCar())) {
            return dtoMapper.objectToDto(carRepository.save(car));
        }else throw new IllegalArgumentException("You are not authorized to edit the car");
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsernameAndIsDeletedFalse(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username is not recognized"));
    }

}

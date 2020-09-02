package com.telerik.carpooling.service;

import com.telerik.carpooling.enums.UserRole;
import com.telerik.carpooling.exception.MyNotFoundException;
import com.telerik.carpooling.model.Car;
import com.telerik.carpooling.model.User;
import com.telerik.carpooling.model.dto.CarDtoEdit;
import com.telerik.carpooling.model.dto.CarDtoRequest;
import com.telerik.carpooling.model.dto.CarDtoResponse;
import com.telerik.carpooling.model.dto.dto.mapper.DtoMapper;
import com.telerik.carpooling.repository.CarRepository;
import com.telerik.carpooling.repository.UserRepository;
import com.telerik.carpooling.service.service.contract.CarService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


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
    public CarDtoResponse getCar(String username) throws MyNotFoundException {

        return dtoMapper.objectToDto(carRepository.findByOwnerAndIsDeletedFalse(username)
                .orElseThrow(()->new MyNotFoundException(username + " does not have a car")));
    }

    @Override
    public CarDtoResponse updateCar(CarDtoEdit carDtoEdit, String loggedUserUsername) {

        Car car = dtoMapper.dtoToObject(carDtoEdit);

        Optional<Car> userCar = carRepository.findByOwnerAndIsDeletedFalse(loggedUserUsername);

        if(userCar.isPresent() && car.equals(userCar.get())) {
            return dtoMapper.objectToDto(carRepository.save(car));
        }else throw new IllegalArgumentException("You are not authorized to edit the car");
    }

    @Override
    public void deleteCar(Long id, String username) throws MyNotFoundException {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new MyNotFoundException("Car with this id not found"));
        User carOwner = car.getOwner();
        User loggedUser = findUserByUsername(username);

        if (isRole_AdminOrSameUser( carOwner, loggedUser)) {
            car.setIsDeleted(true);
            carRepository.save(car);
        } else throw new IllegalArgumentException("You are not authorized to delete the car");
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsernameAndIsDeletedFalse(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username is not recognized"));
    }

    private boolean isRole_AdminOrSameUser( User user, User loggedUser) {

        return loggedUser.equals(user) || loggedUser.getRole().equals(UserRole.ADMIN);
    }

}

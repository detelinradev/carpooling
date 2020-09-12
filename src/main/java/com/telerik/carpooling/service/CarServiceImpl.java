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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Log4j2
@Transactional(readOnly = true)
public class CarServiceImpl implements CarService {


    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final DtoMapper dtoMapper;

    @Override
    @Transactional
    public CarDtoResponse createCar(CarDtoRequest carDtoRequest, String loggedUserUsername) {

        Car car = dtoMapper.dtoToObject(carDtoRequest);
        User owner = findUserByUsername(loggedUserUsername);

        car.setOwner(owner);

        return dtoMapper.objectToDto(carRepository.save(car));
    }

    @Override
    public CarDtoResponse getCar(String loggedUserUsername) throws MyNotFoundException  {

        return dtoMapper.objectToDto(carRepository.findByOwnerAndIsDeletedFalse(loggedUserUsername)
                .orElseThrow(()->new MyNotFoundException(loggedUserUsername + " does not have a car")));
    }

    @Override
    @Transactional
    public CarDtoResponse updateCar(CarDtoEdit carDtoEdit, String loggedUserUsername) {

        Car car = dtoMapper.dtoToObject(carDtoEdit);

        Optional<Car> userCar = carRepository.findByOwnerAndIsDeletedFalse(loggedUserUsername);

        if(userCar.isPresent() && car.equals(userCar.get())) {

            return dtoMapper.objectToDto(carRepository.save(car));

        }else throw new IllegalArgumentException("You are not authorized to edit the car");
    }

    @Override
    @Transactional
    public void deleteCar(Long carId, String loggedUserUsername) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new IllegalArgumentException("Car with this id not found"));
        User carOwner = car.getOwner();
        User loggedUser = findUserByUsername(loggedUserUsername);

        if (isRole_AdminOrSameUser( carOwner, loggedUser)) {

            car.setIsDeleted(true);
            carRepository.save(car);

        } else throw new IllegalArgumentException("You are not authorized to delete the car");
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsernameAndIsDeletedFalse(username)
                .orElseThrow(() -> new IllegalArgumentException("Username is not recognized"));
    }

    private boolean isRole_AdminOrSameUser( User user, User loggedUser) {

        return loggedUser.equals(user) || loggedUser.getRole().equals(UserRole.ADMIN);
    }

}

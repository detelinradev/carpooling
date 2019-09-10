package com.telerik.carpooling.ServiceTests;

import com.telerik.carpooling.models.Car;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.repositories.CarRepository;
import com.telerik.carpooling.repositories.UserRepository;
import com.telerik.carpooling.services.CarServiceImpl;
import javassist.NotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CarServiceTests {
    @Mock
    CarRepository carRepository;
    @Mock
    UserRepository userRepository;

    @Spy
    @InjectMocks
    CarServiceImpl carService;

    private User user;
    private Car car;

    @Before
    public void setUp() {
        user = new User();
        car = new Car();
        car.setModelId(1L);
        user.setUsername("username");
        car.setOwner(user);
        car.setColor("black");
    }

    @Test
    public void get_Should_ReturnCar() throws NotFoundException {
        //Act
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));

        //Assert
        Assert.assertEquals(car, carService.getCar("username1"));
    }

//    @Test//(expected = NullPointerException.class)
//    public void get_Should_ThrowException() {
//        //Act
//        when(carRepository.findById(1L)).thenReturn(null);
//        when(userRepository.findFirstByUsername("username")).thenReturn(null);
//        when(userRepository.findById(1L)).thenReturn(null);
//carService.getCar(user);
//        //Assert
////        Assert.assertEquals(car, carService.getCar(user));
//    }

    @Test
    public void create_Should_ReturnCar() {
        //Act
        Mockito.when(userRepository.findByUsernameAndIsDeletedFalse("username")).thenReturn(Optional.ofNullable(user));
        Mockito.when(carRepository.findById(1L)).thenReturn(Optional.of(car));

//        carService.createCar(car, user);
        //Assert
//        verify(carService, Mockito.times(1)).createCar(car, user);

    }

    @Test
    public void update_Should_ReturnCar() {
        //Act
        Mockito.when(userRepository.findByUsernameAndIsDeletedFalse("username")).thenReturn(Optional.ofNullable(user));
        Mockito.when(carRepository.findById(1L)).thenReturn(Optional.of(car));
//        carService.updateCar(car, user);
        //Assert
//        verify(carService, Mockito.times(1)).updateCar(car, user);
    }

}


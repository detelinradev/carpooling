package com.telerik.carpooling.serviceTests;

import com.telerik.carpooling.enums.UserRole;
import com.telerik.carpooling.exception.MyNotFoundException;
import com.telerik.carpooling.model.Car;
import com.telerik.carpooling.model.User;
import com.telerik.carpooling.model.dto.CarDtoEdit;
import com.telerik.carpooling.model.dto.CarDtoRequest;
import com.telerik.carpooling.model.dto.CarDtoResponse;
import com.telerik.carpooling.model.dto.UserDtoResponse;
import com.telerik.carpooling.model.dto.dto.mapper.DtoMapper;
import com.telerik.carpooling.repository.CarRepository;
import com.telerik.carpooling.repository.UserRepository;
import com.telerik.carpooling.service.CarServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CarServiceTests {

    @Mock
    UserRepository userRepository;

    @Mock
    CarRepository carRepository;

    @Mock
    DtoMapper dtoMapper;

    private Car car1;
    private Car car2;
    private User user1;
    private User user2;
    private CarDtoEdit carDtoEdit;
    private CarDtoResponse carDtoResponse;
    private CarDtoRequest carDtoRequest;

    @Spy
    @InjectMocks
    CarServiceImpl carService;

    @Before
    public void SetUp() {

        user1 = new User("username1", "firstName", "lastName",
                "email@gmail.com", UserRole.USER, "password", "phone", 3.5,
                4.0, 3, 4.0, 3, 4.0);
        user1.setModelId(1L);
        user2 = new User("username2", "lastName", "username2",
                "email@gmail.com", UserRole.USER, "password", "phone", 3.5,
                4.0,
                3, 4.0, 3, 4.0);
        user2.setModelId(2L);
        UserDtoResponse userDtoResponse = new UserDtoResponse(1L, "username1", "firstName", "lastName",
                "email@gmail.com", UserRole.USER, "phone", 3.5,
                4.0);
        userDtoResponse.setModelId(1L);
        carDtoRequest = new CarDtoRequest( "brand","model","color",1980,true);
        car1 = new Car("brand","model","color",1980,true,user1);
        car2 = new Car("brand","model","color",1980,true,user2);
        carDtoEdit = new CarDtoEdit(1L,"brand","model","color",1980);
        carDtoResponse = new CarDtoResponse(1L, "brand","model","color",1980,true);
    }

    @Test
    public void create_Car_Should_CreateNewCar_When_LoggedUserUsernameIsValid()  {

        when(dtoMapper.dtoToObject(carDtoRequest)).thenReturn(car1);
        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(user1));
        when(carRepository.save(car1)).thenReturn(car1);
        when(dtoMapper.objectToDto(car1)).thenReturn(carDtoResponse);

        Assert.assertEquals(carDtoResponse,carService.createCar(carDtoRequest,"username1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void create_Car_Should_ThrowException_IfUserUsernameIsNotValid() {

        when(dtoMapper.dtoToObject(carDtoRequest)).thenReturn(car1);
        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.empty());

        carService.createCar(carDtoRequest, "username1");
    }

    @Test
    public void get_Car_Should_FindCar_When_LoggedUserUsernameIsValid() throws MyNotFoundException {

        when(carRepository.findByOwnerAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(car1));
        when(dtoMapper.objectToDto(car1)).thenReturn(carDtoResponse);

        Assert.assertEquals(carDtoResponse,carService.getCar("username1"));
    }

    @Test(expected = MyNotFoundException.class)
    public void get_Car_Should_ThrowException_IfCarIsNotPresent() throws MyNotFoundException {

        when(carRepository.findByOwnerAndIsDeletedFalse("username1")).thenReturn(Optional.empty());

        carService.getCar( "username1");
    }

    @Test
    public void update_Car_Should_UpdateCar_When_LoggedUserUsernameIsValidAndUserIsTheOwner()  {

        when(dtoMapper.dtoToObject(carDtoEdit)).thenReturn(car1);
        when(carRepository.findByOwnerAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(car1));
        when(carRepository.save(car1)).thenReturn(car1);
        when(dtoMapper.objectToDto(car1)).thenReturn(carDtoResponse);

        Assert.assertEquals(carDtoResponse,carService.updateCar(carDtoEdit,"username1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void update_Car_Should_ThrowException_IfUserDoNotHaveCar(){

        when(dtoMapper.dtoToObject(carDtoEdit)).thenReturn(car1);
        when(carRepository.findByOwnerAndIsDeletedFalse("username1")).thenReturn(Optional.empty());

        carService.updateCar( carDtoEdit,"username1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void update_Car_Should_ThrowException_IfUserIsNotTheOwner(){

        when(dtoMapper.dtoToObject(carDtoEdit)).thenReturn(car1);
        when(carRepository.findByOwnerAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(car2));

        carService.updateCar( carDtoEdit,"username2");
    }

    @Test
    public void delete_Car_Should_DeleteCar_When_CarIdIsValidAndLoggedUserSameUser() {

        when(carRepository.findById(1L)).thenReturn(Optional.ofNullable(car1));
        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(user1));
        when(carRepository.save(car1)).thenReturn(car1);

        carService.deleteCar(1L, "username1");

        verify(carRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findByUsernameAndIsDeletedFalse("username1");
        verify(carRepository, times(1)).save(car1);

        verifyNoMoreInteractions(userRepository, carRepository);
    }

    @Test
    public void delete_Car_Should_DeleteCar_When_CarIdIsValidAndLoggedUserAdmin() {

        user2.setRole(UserRole.ADMIN);
        when(carRepository.findById(1L)).thenReturn(Optional.ofNullable(car1));
        when(userRepository.findByUsernameAndIsDeletedFalse("username2")).thenReturn(Optional.ofNullable(user2));
        when(carRepository.save(car1)).thenReturn(car1);

        carService.deleteCar(1L, "username2");

        verify(carRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findByUsernameAndIsDeletedFalse("username2");
        verify(carRepository, times(1)).save(car1);

        verifyNoMoreInteractions(userRepository, carRepository);
    }

    @Test(expected = IllegalArgumentException.class)
    public void delete_Car_Should_ThrowException_IfCarModelIdIsNotValid() {

        when(carRepository.findById(1L)).thenReturn(Optional.empty());

        carService.deleteCar(1L, "username1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void delete_Car_Should_ThrowException_IfLoggedUserUsernameIsNotValid() {

        when(carRepository.findById(1L)).thenReturn(Optional.ofNullable(car1));
        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.empty());

        carService.deleteCar(1L, "username1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void delete_Car_Should_ThrowException_IfLoggedUserIsNotAuthorOrAdmin() {

        when(carRepository.findById(1L)).thenReturn(Optional.ofNullable(car1));
        when(userRepository.findByUsernameAndIsDeletedFalse("username2")).thenReturn(Optional.ofNullable(user2));

        carService.deleteCar(1L, "username2");
    }
}


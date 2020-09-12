package com.telerik.carpooling.ServiceTests;

import com.telerik.carpooling.enums.UserRole;
import com.telerik.carpooling.exception.FileStorageException;
import com.telerik.carpooling.model.Car;
import com.telerik.carpooling.model.Image;
import com.telerik.carpooling.model.User;
import com.telerik.carpooling.repository.CarRepository;
import com.telerik.carpooling.repository.ImageRepository;
import com.telerik.carpooling.repository.UserRepository;
import com.telerik.carpooling.service.ImageServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.Random;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ImageServiceTests {

    @Mock
    ImageRepository imageRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    CarRepository carRepository;

    private User user;
    private Car car;
    private Image imageUser;
    private Image imageCar;
    private MultipartFile multipartFile;
    private MultipartFile emptyMultipartFile;


    @Spy
    @InjectMocks
    ImageServiceImpl imageService;

    @Before
    public void SetUp() {

        byte[] content = new byte[20];
        byte[] emptyContent = new byte[20];
        new Random().nextBytes(content);
        user = new User("username1", "firstName", "lastName",
                "email@gmail.com", UserRole.USER, "password", "phone", 3.5,
                4.0,3, 4.0, 3, 4.0);
        car = new Car("model", "brand", "color", 2018,true, user);
        multipartFile = new MockMultipartFile("name","fileName","picture",content);
        emptyMultipartFile = new MockMultipartFile("name",content);
        imageUser = new Image("fileName", "picture", content, user);
        imageCar = new Image("fileName", "picture", content, car);
    }

    @Test
    public void store_UserImage_Should_StoreUserImage_When_UserNameIsValidAndFileIsValid() {

        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(user));

        when(imageRepository.save(imageUser)).thenReturn(imageUser);

        imageService.storeUserImage(multipartFile, "username1");


        verify(userRepository, times(1)).findByUsernameAndIsDeletedFalse("username1");
        verify(imageRepository, times(1)).save(imageUser);

        verifyNoMoreInteractions(userRepository, imageRepository);
    }

    @Test(expected = IllegalArgumentException.class)
    public void store_UserImage_Should_ThrowException_IfUsernameIsNotValid() {

        when(userRepository.findByUsernameAndIsDeletedFalse("username1"))
                .thenReturn(Optional.empty());

        imageService.storeUserImage(multipartFile, "username1");
    }

    @Test(expected = FileStorageException.class)
    public void store_UserImage_Should_ThrowException_IfFileIsNotFound() {

        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(user));

        when(imageRepository.save(imageUser)).thenThrow(FileStorageException.class);

        imageService.storeUserImage(multipartFile, "username1");
    }

    @Test
    public void store_CarImage_Should_StoreCarImage_When_UserNameIsValidAndFileIsValidAndCarIsPresent() {

        when(carRepository.findByOwnerAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(car));

        when(imageRepository.save(imageCar)).thenReturn(imageCar);

        imageService.storeCarImage(multipartFile, "username1");


        verify(carRepository, times(1)).findByOwnerAndIsDeletedFalse("username1");
        verify(imageRepository, times(1)).save(imageCar);

        verifyNoMoreInteractions(userRepository, imageRepository);
    }

    @Test
    public void store_CarImage_Should_NotStoreImage_When_CarIsNotPresent() {

        when(carRepository.findByOwnerAndIsDeletedFalse("username1")).thenReturn(Optional.empty());

        imageService.storeCarImage(multipartFile, "username1");

        verify(carRepository, times(1)).findByOwnerAndIsDeletedFalse("username1");

        verifyNoMoreInteractions(userRepository, imageRepository, carRepository);
    }

    @Test(expected = FileStorageException.class)
    public void store_CarImage_Should_ThrowException_IfFileIsNotFound() {

        when(carRepository.findByOwnerAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(car));

        when(imageRepository.save(imageCar)).thenThrow(FileStorageException.class);

        imageService.storeCarImage(multipartFile, "username1");
    }
}
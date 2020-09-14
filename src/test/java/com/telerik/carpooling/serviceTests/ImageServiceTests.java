package com.telerik.carpooling.serviceTests;

import com.telerik.carpooling.enums.UserRole;
import com.telerik.carpooling.exception.FileStorageException;
import com.telerik.carpooling.exception.MyNotFoundException;
import com.telerik.carpooling.model.Car;
import com.telerik.carpooling.model.Image;
import com.telerik.carpooling.model.User;
import com.telerik.carpooling.repository.CarRepository;
import com.telerik.carpooling.repository.ImageRepository;
import com.telerik.carpooling.repository.UserRepository;
import com.telerik.carpooling.service.ImageServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    private User user1;
    private User user2;
    private Car car;
    private Image imageUser;
    private Image imageCar;
    private MultipartFile multipartFile;


    @Spy
    @InjectMocks
    ImageServiceImpl imageService;

    @Before
    public void SetUp() {

        byte[] content = new byte[20];
        new Random().nextBytes(content);
        user1 = new User("username1", "firstName", "lastName",
                "email@gmail.com", UserRole.USER, "password", "phone", 3.5,
                4.0,3, 4.0, 3, 4.0);
        user2 = new User("username2", "firstName", "lastName",
                "email@gmail.com", UserRole.ADMIN, "password", "phone", 3.5,
                4.0,3, 4.0, 3, 4.0);
        car = new Car("model", "brand", "color", 2018,true, user1);
        multipartFile = new MockMultipartFile("name","fileName","picture",content);
        imageUser = new Image("fileName", "picture", content, user1);
        imageCar = new Image("fileName", "picture", content, car);
    }

    @Test
    public void store_UserImage_Should_StoreUserImage_When_UserNameIsValidAndFileIsValid() {

        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(user1));

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

        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(user1));
        when(imageRepository.save(imageUser)).thenAnswer(invocation -> {throw new IOException("");});

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

        when(imageRepository.save(imageCar)).thenAnswer(invocation -> {throw new IOException("");});

        imageService.storeCarImage(multipartFile, "username1");
    }

    @Test
    public void get_UserImage_Should_RetrieveUserImage_When_UsernameIsValidAndUserHaveImage() throws MyNotFoundException {

        when(userRepository.findByUsernameAndIsDeletedFalse(user1.getUsername()))
                .thenReturn(java.util.Optional.ofNullable(user1));
        when(imageRepository.findByUserAndIsDeletedFalse(user1)).thenReturn(Optional.ofNullable(imageUser));

        Assert.assertEquals(imageUser, imageService.getUserImage("username1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void get_UserImage_Should_ThrowException_IfUsernameIsNotValid() throws MyNotFoundException {

        when(userRepository.findByUsernameAndIsDeletedFalse(user1.getUsername()))
                .thenReturn(Optional.empty());

        imageService.getUserImage("username1");
    }

    @Test(expected = MyNotFoundException.class)
    public void get_UserImage_Should_ThrowException_IfUserDoNotHaveImage() throws MyNotFoundException {

        when(userRepository.findByUsernameAndIsDeletedFalse(user1.getUsername()))
                .thenReturn(java.util.Optional.ofNullable(user1));
        when(imageRepository.findByUserAndIsDeletedFalse(user1)).thenAnswer(invocation -> {throw new MyNotFoundException("");});

        imageService.getUserImage("username1");
    }

    @Test
    public void get_CarImage_Should_RetrieveCarImage_When_UsernameIsValidAndCarHaveImage() throws MyNotFoundException {

        when(userRepository.findByUsernameAndIsDeletedFalse(user1.getUsername()))
                .thenReturn(java.util.Optional.ofNullable(user1));
        when(carRepository.findByOwnerAndIsDeletedFalse(user1.getUsername())).thenReturn(Optional.ofNullable(car));
        when(imageRepository.findByCarAndIsDeletedFalse(car)).thenReturn(Optional.ofNullable(imageCar));

        Assert.assertEquals(imageCar, imageService.getCarImage("username1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void get_CarImage_Should_ThrowException_IfUsernameIsNotValid() throws MyNotFoundException {

        when(userRepository.findByUsernameAndIsDeletedFalse(user1.getUsername()))
                .thenReturn(Optional.empty());

        imageService.getCarImage("username1");
    }

    @Test(expected = MyNotFoundException.class)
    public void get_CarImage_Should_ThrowException_IfCarDoNotHaveImage() throws MyNotFoundException {

        when(userRepository.findByUsernameAndIsDeletedFalse(user1.getUsername()))
                .thenReturn(java.util.Optional.ofNullable(user1));
        when(carRepository.findByOwnerAndIsDeletedFalse(user1.getUsername())).thenReturn(Optional.ofNullable(car));
        when(imageRepository.findByUserAndIsDeletedFalse(user1)).thenAnswer(invocation -> {throw new MyNotFoundException("");});

        imageService.getCarImage("username1");
    }

    @Test
    public void delete_UserImage_Should_DeleteUserImage_When_UsernamesAreValidAndUserHasImageAndUserIsSameUser() {

        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(user1));
        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(user1));
        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(user1));
        when(imageRepository.findByUserAndIsDeletedFalse(user1)).thenReturn(Optional.ofNullable(imageUser));
        when(imageRepository.save(imageUser)).thenReturn(imageUser);

        imageService.deleteUserImage("username1", "username1");

        verify(userRepository, times(3)).findByUsernameAndIsDeletedFalse("username1");
        verify(imageRepository, times(1)).findByUserAndIsDeletedFalse(user1);
        verify(imageRepository, times(1)).save(imageUser);

        verifyNoMoreInteractions(userRepository, imageRepository);
    }

    @Test
    public void delete_UserImage_Should_DeleteUserImage_When_UsernamesAreValidAndUserHasImageAndUserIsAdmin() {

        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(user1));
        when(userRepository.findByUsernameAndIsDeletedFalse("username2")).thenReturn(Optional.ofNullable(user2));
        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(user1));
        when(imageRepository.findByUserAndIsDeletedFalse(user1)).thenReturn(Optional.ofNullable(imageUser));
        when(imageRepository.save(imageUser)).thenReturn(imageUser);

        imageService.deleteUserImage("username1", "username2");

        verify(userRepository, times(2)).findByUsernameAndIsDeletedFalse("username1");
        verify(userRepository, times(1)).findByUsernameAndIsDeletedFalse("username2");
        verify(imageRepository, times(1)).findByUserAndIsDeletedFalse(user1);
        verify(imageRepository, times(1)).save(imageUser);

        verifyNoMoreInteractions(userRepository, imageRepository);
    }

    @Test(expected = IllegalArgumentException.class)
    public void delete_UserImage_Should_ThrowException_IfUsernameIsNotValid()  {

        when(userRepository.findByUsernameAndIsDeletedFalse("username1"))
                .thenReturn(Optional.empty());

        imageService.deleteUserImage("username1","username1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void delete_UserImage_Should_ThrowException_IfLoggedUserUsernameIsNotValid()  {

        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(user1));
        when(userRepository.findByUsernameAndIsDeletedFalse("username2"))
                .thenReturn(Optional.empty());

        imageService.deleteUserImage("username1","username2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void delete_UserImage_Should_ThrowException_IfUserDoNotHaveImage()  {

        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(user1));
        when(userRepository.findByUsernameAndIsDeletedFalse("username2")).thenReturn(Optional.ofNullable(user2));
        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(user1));
        when(imageRepository.findByUserAndIsDeletedFalse(user1)).thenAnswer(invocation -> {throw new MyNotFoundException("");});

        imageService.deleteUserImage("username1","username2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void delete_UserImage_Should_ThrowException_IfLoggedUserUsernameIsNotSameUserAndNotAdmin()  {

        user2.setRole(UserRole.USER);
        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(user1));
        when(userRepository.findByUsernameAndIsDeletedFalse("username2")).thenReturn(Optional.ofNullable(user2));

        imageService.deleteUserImage("username1","username2");
    }

    @Test
    public void delete_CarImage_Should_DeleteCarImage_When_UsernamesAreValidAndCarHasImageAndUserIsSameUser() {

        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(user1));
        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(user1));
        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(user1));
        when(carRepository.findByOwnerAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(car));
        when(imageRepository.findByCarAndIsDeletedFalse(car)).thenReturn(Optional.ofNullable(imageCar));
        when(imageRepository.save(imageCar)).thenReturn(imageCar);

        imageService.deleteCarImage("username1", "username1");

        verify(userRepository, times(3)).findByUsernameAndIsDeletedFalse("username1");
        verify(imageRepository, times(1)).findByCarAndIsDeletedFalse(car);
        verify(imageRepository, times(1)).save(imageCar);

        verifyNoMoreInteractions(userRepository, imageRepository);
    }

    @Test
    public void delete_CarImage_Should_DeleteCarImage_When_UsernamesAreValidAndCarHasImageAndUserIsAdmin() {

        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(user1));
        when(userRepository.findByUsernameAndIsDeletedFalse("username2")).thenReturn(Optional.ofNullable(user2));
        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(user1));
        when(carRepository.findByOwnerAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(car));
        when(imageRepository.findByCarAndIsDeletedFalse(car)).thenReturn(Optional.ofNullable(imageCar));
        when(imageRepository.save(imageCar)).thenReturn(imageCar);

        imageService.deleteCarImage("username1", "username2");

        verify(userRepository, times(2)).findByUsernameAndIsDeletedFalse("username1");
        verify(userRepository, times(1)).findByUsernameAndIsDeletedFalse("username2");
        verify(imageRepository, times(1)).findByCarAndIsDeletedFalse(car);
        verify(imageRepository, times(1)).save(imageCar);

        verifyNoMoreInteractions(userRepository, imageRepository);
    }

    @Test(expected = IllegalArgumentException.class)
    public void delete_CarImage_Should_ThrowException_IfUsernameIsNotValid()  {

        when(userRepository.findByUsernameAndIsDeletedFalse("username1"))
                .thenReturn(Optional.empty());

        imageService.deleteCarImage("username1","username1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void delete_CarImage_Should_ThrowException_IfLoggedUserUsernameIsNotValid()  {

        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(user1));
        when(userRepository.findByUsernameAndIsDeletedFalse("username2"))
                .thenReturn(Optional.empty());

        imageService.deleteCarImage("username1","username2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void delete_CarImage_Should_ThrowException_IfCarDoNotHaveImage()  {

        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(user1));
        when(userRepository.findByUsernameAndIsDeletedFalse("username2")).thenReturn(Optional.ofNullable(user2));
        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(user1));
        when(imageRepository.findByCarAndIsDeletedFalse(car)).thenAnswer(invocation -> {throw new MyNotFoundException("");});

        imageService.deleteCarImage("username1","username2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void delete_CarImage_Should_ThrowException_IfLoggedUserUsernameIsNotSameUserAndNotAdmin()  {

        user2.setRole(UserRole.USER);
        when(userRepository.findByUsernameAndIsDeletedFalse("username1")).thenReturn(Optional.ofNullable(user1));
        when(userRepository.findByUsernameAndIsDeletedFalse("username2")).thenReturn(Optional.ofNullable(user2));

        imageService.deleteCarImage("username1","username2");
    }
}
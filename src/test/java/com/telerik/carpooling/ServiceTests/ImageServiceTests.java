package com.telerik.carpooling.ServiceTests;

import com.telerik.carpooling.models.*;
import com.telerik.carpooling.models.dtos.CarDtoRequest;
import com.telerik.carpooling.models.dtos.CarDtoResponse;
import com.telerik.carpooling.models.dtos.UserDtoRequest;
import com.telerik.carpooling.models.dtos.UserDtoResponse;
import com.telerik.carpooling.models.dtos.dtos.mapper.DtoMapper;
import com.telerik.carpooling.repositories.ImageRepository;
import com.telerik.carpooling.repositories.UserRepository;
import com.telerik.carpooling.services.ImageServiceImpl;
import com.telerik.carpooling.services.services.contracts.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.client.HttpClientErrorException;

import java.net.URI;
import java.util.Optional;
import java.util.Random;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ImageServiceTests {

    @Mock
    ImageRepository imageRepository;
    @Mock
    UserService userService;
    @Mock
    UserRepository userRepository;
    @Mock
    DtoMapper dtoMapper;

    private User user;
    private Car car;
    private Image image;


    @Spy
    @InjectMocks
    ImageServiceImpl imageService;

    @Before
    public void SetUp() {
        MockitoAnnotations.initMocks(this);

        byte[] content = new byte[20];
        new Random().nextBytes(content);
        this.user = new User("firstName", "lastName", "username1",
                "email@gmail.com", "0888999888", 3.2, 3.3,
                "Password!1", "USER", null, "URI", null,
                null, null, image, car);

        this.image = new Image("fileName", "picture", content, user, null);
        this.image.setModelId(1L);

        this.car = new Car("model", "brand", "color", 2018, "yes", image, user,null );
    }

    @Test (expected = NullPointerException.class)
    public void Should_ThrowException_When_FileIsEmpty() {
        //Arrange
//        final Long authorId = 1L;
//        final Long userId = authorId;
        User author = new User();
        author.setUsername("username1");
        byte[] content = null;
        final String name = "picture.jpg";
        final String type = "image/jpeg";
        URI uri = URI.create("username1");
        MockMultipartFile file = null;

        when(userService.getUser("username1")).thenReturn(author);

        //Act & Assert
        imageService.storeUserImage(file, author, uri);

    }

//    @Test
//    public void getByID_Should_Return_Beer_When_Beer_With_Same_ID_Exists() throws IllegalArgumentException {
//        Mockito.when(imageRepository.findById(1L)).thenReturn(Optional.of(image));
//        imageService.getImage(1L);
//        Assert.assertEquals(image, imageService.getImage(1L));
//    }


    @Test(expected = NullPointerException.class)
    public void saveUserImage_Should_ThrowException_When_FileIsEmpty() {
        //Arrange
        User author = new User();
        author.setUsername("username");
        byte[] content = null;
        final String name = "picture.jpg";
        final String type = "image/jpeg";
        URI uri = URI.create(author.getAvatarUri());
        MockMultipartFile file = new MockMultipartFile(name, name, type, content);

        when(userService.getUser("username")).thenReturn(author);
        when(imageService.storeUserImage(file, author, uri)).thenReturn(null);

        //Act & Assert
        imageService.storeUserImage(file, author, uri);

    }

    //
    @Test
    public void save_Should_Return_Picture_When_Successful() {
        //Arrange
        User author = new User();
        author.setUsername("username");
        byte[] content = new byte[20];
        new Random().nextBytes(content);
        final String name = "picture.jpg";
        final String type = "image/jpeg";
        URI uri = URI.create("test");
        MockMultipartFile file = new MockMultipartFile(name,name, type, content);

        Image image = new Image();
        image.setModelId(1L);
        when(userService.getUser("username")).thenReturn(author);
        when(imageService.getImage(1L)).thenReturn(image);
        //Act
        Image result = imageService.storeUserImage(file, author, uri);
        System.out.println(result);
        //Assert
        Assert.assertEquals(result, imageService.getImage(1L));
    }
//
//    @Test (expected = NotFoundException.class)
//    public void findByUserId_Should_ThrowException_When_NotFound() {
//        //Arrange
//        final Long userId = 1L;
//        User user = new User();
//        user.setId(userId);
//
//        when(userService.findById(userId)).thenReturn(user);
//
//        //Act & Assert
//        pictureService.findByUserId(userId);
//    }
//
//
//    @Test
//    public void findByUserId_Should_Return_Resource_When_Successful() {
//        //Arrange
//        final Long userId = 1L;
//        final String fileName = "Picture";
//        User user = new User();
//        user.setId(userId);
//        Picture picture = new Picture();
//        picture.setFileName(fileName);
//
//        Resource resource = null;
//
//        when(userService.findById(userId)).thenReturn(user);
//        when(pictureService.loadFileByName(fileName)).thenReturn(resource);
//
//        //Act
//        Resource result = pictureService.findByUserId(userId);
//
//        //Assert
//        Assert.assertEquals(result, resource);
//    }
}
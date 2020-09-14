package com.telerik.carpooling.controllerTests;

import com.telerik.carpooling.controller.ImageController;
import com.telerik.carpooling.enums.UserRole;
import com.telerik.carpooling.model.Image;
import com.telerik.carpooling.model.User;
import com.telerik.carpooling.service.service.contract.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
public class ImageControllerTests {

    private MockMvc mockMvc;

    @Mock
    ImageService imageService;

    @InjectMocks
    ImageController imageController;

    private Image image;
    private Authentication authentication;

    @BeforeEach
    public void setup() {

        mockMvc = MockMvcBuilders.standaloneSetup(imageController)
                .build();
        authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = new User("username1", "lastName", "username1",
                "email@gmail.com", UserRole.USER, "password", "phone", 3.5,
                4.0, 3, 4.0, 3, 4.0);
        image = new Image("fileName", "image/jpeg", "picture".getBytes(), user);
    }

    @Test
    public void get_UserImage_Should_ReturnUserImage() throws Exception {
        // given
        given(imageService.getUserImage("username1"))
                .willReturn(image);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                get("/images/username1")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsByteArray()).isEqualTo(
                image.getData()
        );
    }

    @Test
    public void get_CarImage_Should_ReturnCarImage() throws Exception {
        // given
        given(imageService.getCarImage("username1"))
                .willReturn(image);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                get("/images/car/username1")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsByteArray()).isEqualTo(
                image.getData()
        );
    }

    @Test
    public void delete_CarImage_Should_DeleteCarImage() throws Exception {

        // when
        when(authentication.getName()).thenReturn("username1");
        MockHttpServletResponse response = mockMvc.perform(
                delete("/images/car/username1")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void delete_UserImage_Should_DeleteUserImage() throws Exception {

        // when
        when(authentication.getName()).thenReturn("username1");
        MockHttpServletResponse response = mockMvc.perform(
                delete("/images/username1")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void upload_UserImage_Should_UploadUserImage() throws Exception {

        when(authentication.getName()).thenReturn("username1");
        MockHttpServletResponse response = mockMvc.perform(
                multipart("/images").file("upFile","mockMultipartFile".getBytes()))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void upload_CarImage_Should_UploadCarImage() throws Exception {

        // when
        when(authentication.getName()).thenReturn("username1");
        MockHttpServletResponse response = mockMvc.perform(
                multipart("/images/car").file("upFile","mockMultipartFile".getBytes()))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}

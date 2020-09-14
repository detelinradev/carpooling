package com.telerik.carpooling.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telerik.carpooling.controller.CarController;
import com.telerik.carpooling.enums.UserRole;
import com.telerik.carpooling.model.dto.CarDtoEdit;
import com.telerik.carpooling.model.dto.CarDtoRequest;
import com.telerik.carpooling.model.dto.CarDtoResponse;
import com.telerik.carpooling.model.dto.UserDtoResponse;
import com.telerik.carpooling.service.service.contract.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
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
public class CarControllerTests {

    private MockMvc mockMvc;

    @Mock
    CarService carService;


    @InjectMocks
    CarController carController;

    private JacksonTester<CarDtoResponse> jsonCarDtoResponse;
    private JacksonTester<CarDtoEdit> jsonCarDtoEdit;
    private JacksonTester<CarDtoRequest> jsonCarDtoRequest;
    private CarDtoResponse carDtoResponse;
    private CarDtoEdit carDtoEdit;
    private CarDtoRequest carDtoRequest;
    private Authentication authentication;

    @BeforeEach
    public void setup() {

        mockMvc = MockMvcBuilders.standaloneSetup(carController)
                .build();
        authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        JacksonTester.initFields(this, new ObjectMapper());
        UserDtoResponse userDtoResponse = new UserDtoResponse(1L, "username1", "lastName",
                "username1", "email@gmail.com", UserRole.USER, "0889111777", 3.5,
                4.0);
        carDtoEdit = new CarDtoEdit(1L,"brand","model","color",1980);
        carDtoResponse = new CarDtoResponse(1L, "brand","model","color",
                1980,true);
        carDtoRequest = new CarDtoRequest( "brand","model","color",1980,true);
    }

    @Test
    public void get_Car_Should_ReturnCar() throws Exception {
        // given
        given(carService.getCar("username1"))
                .willReturn(carDtoResponse);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                get("/car/username1")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonCarDtoResponse.write(carDtoResponse).getJson()
        );
    }

    @Test
    public void update_Car_Should_UpdateCar() throws Exception {
        // given
        given(carService.updateCar(carDtoEdit,"username1"))
                .willReturn(carDtoResponse);

        // when
        when(authentication.getName()).thenReturn("username1");
        MockHttpServletResponse response = mockMvc.perform(
                put("/car")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCarDtoEdit.write(carDtoEdit).getJson())
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonCarDtoResponse.write(carDtoResponse).getJson()
        );
    }

    @Test
    public void delete_Car_Should_DeleteCar() throws Exception {

        // when
        when(authentication.getName()).thenReturn("username1");
        MockHttpServletResponse response = mockMvc.perform(
                delete("/car/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void create_Car_Should_CreateCar() throws Exception {
        // given
        given(carService.createCar(carDtoRequest,"username1"))
                .willReturn(carDtoResponse);

        // when
        when(authentication.getName()).thenReturn("username1");
        MockHttpServletResponse response = mockMvc.perform(
                post("/car")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCarDtoRequest.write(carDtoRequest).getJson())
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonCarDtoResponse.write(carDtoResponse).getJson()
        );
    }
}

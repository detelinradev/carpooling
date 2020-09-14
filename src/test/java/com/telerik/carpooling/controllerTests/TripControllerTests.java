package com.telerik.carpooling.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.telerik.carpooling.controller.TripController;
import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.enums.UserRole;
import com.telerik.carpooling.enums.UserStatus;
import com.telerik.carpooling.model.Trip;
import com.telerik.carpooling.model.dto.*;
import com.telerik.carpooling.service.service.contract.TripService;
import com.telerik.carpooling.service.service.contract.TripUserStatusService;
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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
public class TripControllerTests {

    private MockMvc mockMvc;

    @Mock
    TripService tripService;

    @Mock
    TripUserStatusService tripUserStatusService;

    @InjectMocks
    TripController tripController;

    private JacksonTester<TripDtoRequest> jsonTripDtoRequest;
    private JacksonTester<TripDtoResponse> jsonTripDtoResponse;
    private JacksonTester<TripUserStatusDtoResponse> jsonTripUserStatusDtoResponse;
    private JacksonTester<TripDtoEdit> jsonTripDtoEdit;
    private JacksonTester<List<TripUserStatusDtoResponse>> jsonTripUserStatusDtoResponseList;
    private Trip trip;
    private TripDtoRequest tripDtoRequest;
    private TripDtoEdit tripDtoEdit;
    private TripDtoResponse tripDtoResponse;
    private TripUserStatusDtoResponse tripUserStatusDtoResponse;
    private List<TripUserStatusDtoResponse> tripUserStatusDtoResponseList;
    private Authentication authentication;

    @BeforeEach
    public void setup()  {

        mockMvc = MockMvcBuilders.standaloneSetup(tripController)
                .build();
        authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        JacksonTester.initFields(this, objectMapper);
        UserDtoResponse userDtoResponse = new UserDtoResponse(1L, "username1", "lastName",
                "username1", "email@gmail.com", UserRole.USER, "0889111777", 3.5,
                4.0);
        trip = new Trip("message", LocalDateTime.MAX,
                "origin", "destination", 3, 5,4,
                true, true,true,true,TripStatus.AVAILABLE);
        tripDtoRequest = new TripDtoRequest("message", LocalDateTime.MAX.truncatedTo(ChronoUnit.MINUTES),
                "origin","destination", 3,5,
                4, true, true, true,true);
        tripDtoEdit = new TripDtoEdit(1L, "message", LocalDateTime.MAX.truncatedTo(ChronoUnit.MINUTES),
                "origin","destination", 3, 4,
                4, true, true, true,true);
        tripDtoResponse = new TripDtoResponse(1L, "message", LocalDateTime.MAX.truncatedTo(ChronoUnit.MINUTES),
                "origin","destination", 3, TripStatus.AVAILABLE, 4,
                4, true, true, true,true);
        tripUserStatusDtoResponse = new TripUserStatusDtoResponse(1L, tripDtoResponse, userDtoResponse,
                UserStatus.DRIVER);
        tripUserStatusDtoResponseList = new ArrayList<>();
        tripUserStatusDtoResponseList.add(tripUserStatusDtoResponse);
    }

    @Test
    public void get_AllTripUserStatuses_Should_ReturnListWithTripUserStatuses() throws Exception {
        // given
        given(tripUserStatusService.getAllTripsWithDrivers(0,10,TripStatus.AVAILABLE,null,
                null,null,null,null,null,
                null,null,null))
                .willReturn(tripUserStatusDtoResponseList);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                get("/trips?")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonTripUserStatusDtoResponseList.write(tripUserStatusDtoResponseList).getJson()
        );
    }

    @Test
    public void get_AllTripUserStatusesForATrip_Should_ReturnListWithTripUserStatuses() throws Exception {
        // given
        given(tripUserStatusService.getCurrentTripUserStatusForAllUsersInATrip(1L))
                .willReturn(tripUserStatusDtoResponseList);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                get("/trips/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonTripUserStatusDtoResponseList.write(tripUserStatusDtoResponseList).getJson()
        );
    }

    @Test
    public void get_MyTrips_Should_ReturnListWithTripUserStatuses() throws Exception {
        // given
        given(tripUserStatusService.getUserOwnTripsWithDrivers("username1"))
                .willReturn(tripUserStatusDtoResponseList);

        // when
        when(authentication.getName()).thenReturn("username1");
        MockHttpServletResponse response = mockMvc.perform(
                get("/trips/myTrips")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonTripUserStatusDtoResponseList.write(tripUserStatusDtoResponseList).getJson()
        );
    }

    @Test
    public void update_Trip_Should_UpdateTrip() throws Exception {
        // given
        given(tripService.updateTrip(tripDtoEdit,"username1"))
                .willReturn(tripDtoResponse);

        // when
        when(authentication.getName()).thenReturn("username1");
        MockHttpServletResponse response = mockMvc.perform(
                put("/trips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTripDtoEdit.write(tripDtoEdit).getJson())
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonTripDtoResponse.write(tripDtoResponse).getJson()
        );
    }

    @Test
    public void change_TripStatus_Should_ChangeTripStatus() throws Exception {

        // when
        when(authentication.getName()).thenReturn("username1");
        MockHttpServletResponse response = mockMvc.perform(
                patch("/trips/1?status=AVAILABLE")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void change_UserStatus_Should_ChangeUserStatus() throws Exception {

        // when
        when(authentication.getName()).thenReturn("username1");
        MockHttpServletResponse response = mockMvc.perform(
                patch("/trips/1/passengers/username1/?status=DRIVER")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void delete_Trip_Should_DeleteTrip() throws Exception {

        // when
        when(authentication.getName()).thenReturn("username1");
        MockHttpServletResponse response = mockMvc.perform(
                patch("/trips/1/delete")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void create_Trip_Should_CreateTrip() throws Exception {
        // given
        given(tripService.createTrip(tripDtoRequest,"username1"))
                .willReturn(trip);
        given(tripUserStatusService.createTripUserStatusAsDriver(trip,"username1"))
                .willReturn(tripUserStatusDtoResponse);

        // when
        when(authentication.getName()).thenReturn("username1");
        MockHttpServletResponse response = mockMvc.perform(
                post("/trips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTripDtoRequest.write(tripDtoRequest).getJson())
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonTripUserStatusDtoResponse.write(tripUserStatusDtoResponse).getJson()
        );
    }

}


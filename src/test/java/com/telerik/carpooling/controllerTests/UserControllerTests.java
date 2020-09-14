package com.telerik.carpooling.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telerik.carpooling.controller.UserController;
import com.telerik.carpooling.enums.UserRole;
import com.telerik.carpooling.model.dto.FeedbackDtoResponse;
import com.telerik.carpooling.model.dto.UserDtoEdit;
import com.telerik.carpooling.model.dto.UserDtoRequest;
import com.telerik.carpooling.model.dto.UserDtoResponse;
import com.telerik.carpooling.service.service.contract.FeedbackService;
import com.telerik.carpooling.service.service.contract.RatingService;
import com.telerik.carpooling.service.service.contract.UserService;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@ExtendWith(MockitoExtension.class)
public class UserControllerTests {

    private MockMvc mockMvc;

    @Mock
    UserService userService;

    @Mock
    FeedbackService feedbackService;

    @Mock
    RatingService ratingService;

    @InjectMocks
    UserController userController;

    private JacksonTester<UserDtoResponse> jsonUserDtoResponse;
    private JacksonTester<UserDtoRequest> jsonUserDtoRequest;
    private JacksonTester<List<UserDtoResponse>> jsonUserList;
    private JacksonTester<Set<FeedbackDtoResponse>> jsonFeedbackSet;
    private UserDtoEdit userDtoEdit;
    private UserDtoResponse userDtoResponse;
    private UserDtoRequest userDtoRequest;
    private Authentication authentication;
    private List<UserDtoResponse> userDtoResponseList;
    private Set<FeedbackDtoResponse> feedbackDtoResponseSet;

    @BeforeEach
    public void setup()  {

        JacksonTester.initFields(this, new ObjectMapper());
        userDtoEdit = new UserDtoEdit(1L,"email@gmail.com", UserRole.USER,null, "0889111777");
        userDtoResponse = new UserDtoResponse(1L, "username1", "lastName", "username1",
                "email@gmail.com", UserRole.USER, "0889111777", 3.5, 4.0);
        userDtoRequest = new UserDtoRequest("username1", "lastName", "username1",
                "email@gmail.com", "Password!1", "0889111777");
        FeedbackDtoResponse feedbackDtoResponse = new FeedbackDtoResponse(1L, userDtoResponse, "feedback");
        userDtoResponseList = new ArrayList<>();
        feedbackDtoResponseSet = new HashSet<>();
        userDtoResponseList.add(userDtoResponse);
        feedbackDtoResponseSet.add(feedbackDtoResponse);
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .build();
        authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);

    }

    @Test
    public void get_User_Should_ReturnUser() throws Exception {
        // given
        given(userService.getUser("username1","username1"))
                .willReturn(userDtoResponse);

        // when
        when(authentication.getName()).thenReturn("username1");
        MockHttpServletResponse response = mockMvc.perform(
                 get("/users/username1")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonUserDtoResponse.write(userDtoResponse).getJson()
        );
    }

    @Test
    public void get_Users_Should_ReturnListWithUsers() throws Exception {
        // given
        given(userService.getUsers(0,10,null,null,null,null,null))
                .willReturn(userDtoResponseList);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                get("/users?")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonUserList.write(userDtoResponseList).getJson()
        );
    }

    @Test
    public void get_TopRatedUsers_Should_ReturnListWithUsers() throws Exception {
        // given
        given(userService.getTopRatedUsers(true))
                .willReturn(userDtoResponseList);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                get("/users/top-rated-users?isPassenger=true")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonUserList.write(userDtoResponseList).getJson()
        );
    }

    @Test
    public void get_Feedback_Should_ReturnSetWithFeedback() throws Exception {
        // given
        given(feedbackService.getFeedback("username1"))
                .willReturn(feedbackDtoResponseSet);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                get("/users/username1/feedback")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonFeedbackSet.write(feedbackDtoResponseSet).getJson()
        );
    }

    @Test
    public void update_User_Should_UpdateUser() throws Exception {
        // given
        given(userService.updateUser(userDtoEdit, "username1"))
                .willReturn(userDtoResponse);


        // when
        when(authentication.getName()).thenReturn("username1");
        MockHttpServletResponse response = mockMvc.perform(
                put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUserDtoResponse.write(userDtoResponse).getJson())
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonUserDtoResponse.write(userDtoResponse).getJson()
        );
    }

    @Test
    public void delete_USer_Should_DeleteUser() throws Exception {

        // when
        MockHttpServletResponse response = mockMvc.perform(
                delete("/users/username1/delete")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void create_User_Should_CreateUser() throws Exception {
        // given
        given(userService.createUser(userDtoRequest))
                .willReturn(userDtoResponse);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUserDtoRequest.write(userDtoRequest).getJson())
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonUserDtoResponse.write(userDtoResponse).getJson()
        );
    }

    @Test
    public void rate_User_Should_RateUser() throws Exception {

        // when
        MockHttpServletResponse response = mockMvc.perform(
                post("/users/rate/1/user/username1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("5")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void leave_Feedback_Should_LeaveFeedback() throws Exception {

        // when
        MockHttpServletResponse response = mockMvc.perform(
                post("/users/feedback/1/user/username1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("feedback")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}

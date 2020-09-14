package com.telerik.carpooling.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.telerik.carpooling.controller.CommentController;
import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.enums.UserRole;
import com.telerik.carpooling.enums.UserStatus;
import com.telerik.carpooling.model.Comment;
import com.telerik.carpooling.model.Trip;
import com.telerik.carpooling.model.dto.*;
import com.telerik.carpooling.service.service.contract.CommentService;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
public class CommentControllerTests {

    private MockMvc mockMvc;

    @Mock
    CommentService commentService;

    @Mock
    TripUserStatusService tripUserStatusService;

    @InjectMocks
    CommentController commentController;

    private JacksonTester<CommentDtoResponse> jsonCommentDtoResponse;
    private JacksonTester<CommentDtoEdit> jsonCommentDtoEdit;
    private JacksonTester<Set<CommentDtoResponse>> jsonCommentDtoResponseSet;
    private JacksonTester<TripUserStatusDtoResponse> jsonTripUserStatusDtoResponse;
    private JacksonTester<TripDtoEdit> jsonTripDtoEdit;
    private JacksonTester<List<TripUserStatusDtoResponse>> jsonTripUserStatusDtoResponseList;
    private Comment comment;
    private CommentDtoResponse commentDtoResponse;
    private CommentDtoEdit commentDtoEdit;
    private Trip trip;
    private TripDtoRequest tripDtoRequest;
    private TripDtoEdit tripDtoEdit;
    private TripDtoResponse tripDtoResponse;
    private TripUserStatusDtoResponse tripUserStatusDtoResponse;
    private List<TripUserStatusDtoResponse> tripUserStatusDtoResponseList;
    private Set<CommentDtoResponse> commentDtoResponseSet;
    private Authentication authentication;

    @BeforeEach
    public void setup()  {

        mockMvc = MockMvcBuilders.standaloneSetup(commentController)
                .build();
        authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        JacksonTester.initFields(this, objectMapper);
        UserDtoResponse userDtoResponse = new UserDtoResponse(1L, "username1", "lastName",
                "username1", "email@gmail.com", UserRole.USER, "0889111777", 3.5,
                4.0);
        commentDtoEdit = new CommentDtoEdit(1L,"message");
        commentDtoResponse = new CommentDtoResponse(1L, userDtoResponse,"message");
        commentDtoResponseSet = new HashSet<>();
        commentDtoResponseSet.add(commentDtoResponse);
        trip = new Trip("message", LocalDateTime.MAX,
                "origin", "destination", 3, 5,4,
                true, true,true,true, TripStatus.AVAILABLE);
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
    public void get_Comments_Should_ReturnSetWithComments() throws Exception {
        // given
        given(commentService.getComments(1L))
                .willReturn(commentDtoResponseSet);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                get("/comments/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonCommentDtoResponseSet.write(commentDtoResponseSet).getJson()
        );
    }

    @Test
    public void update_Comments_Should_UpdateComment() throws Exception {
        // given
        given(commentService.updateComment(commentDtoEdit,"username1"))
                .willReturn(commentDtoResponse);

        // when
        when(authentication.getName()).thenReturn("username1");
        MockHttpServletResponse response = mockMvc.perform(
                put("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCommentDtoEdit.write(commentDtoEdit).getJson())
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonCommentDtoResponse.write(commentDtoResponse).getJson()
        );
    }

    @Test
    public void delete_Comments_Should_DeleteComment() throws Exception {

        // when
        when(authentication.getName()).thenReturn("username1");
        MockHttpServletResponse response = mockMvc.perform(
                delete("/comments/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void create_Comments_Should_CreateComment() throws Exception {
        // given
        given(commentService.createComment(1L,"username1","message"))
                .willReturn(commentDtoResponse);

        // when
        when(authentication.getName()).thenReturn("username1");
        MockHttpServletResponse response = mockMvc.perform(
                post("/comments/1?comment=message")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonCommentDtoResponse.write(commentDtoResponse).getJson()
        );
    }
}

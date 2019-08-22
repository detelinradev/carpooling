package com.telerik.carpooling.models.dtos.dtos.mapper;

import com.telerik.carpooling.models.*;
import com.telerik.carpooling.models.dtos.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = { MapperResolver.class })
public interface DtoMapper {

    TripDtoResponse objectToDto(Trip trip);

    Trip dtoToObject(TripDtoEdit tripDtoEdit);

    Trip dtoToObject(TripDtoRequest tripRequestDto);

    CarDtoResponse objectToDto(Car car);

    Car dtoToObject(CarDtoRequest carDtoRequest);

    Car dtoToObject(CarDtoResponse carDtoResponse);

    UserDtoResponse objectToDto(User user);

    User dtoToObject(UserDtoRequest userDtoRequest);

    User dtoToObject(UserDtoResponse userDtoResponse);

    CommentDtoResponse objectToDto(Comment comment);

    Comment dtoToObject(CommentDtoRequest commentDtoRequest);

    Comment dtoToObject(CommentDtoResponse commentDtoResponse);

    FeedbackDtoResponse objectToDto(Feedback feedback);

    Feedback dtoToObject(FeedbackDtoRequest feedbackDtoRequest);

    Feedback dtoToObject(FeedbackDtoResponse feedbackDtoResponse);

    Set<UserDtoResponse> usersToUserDtoResponses(Set<User> passengers);

    Set<CommentDtoResponse> commentsToCommentsDtoResponses(Set<Comment>comments);

    Set<FeedbackDtoResponse> feedbackToFeedbackDtoResponses(Set<Feedback>feedback);

    List<TripDtoResponse> tripToDtoList(List<Trip> trips);

    List<UserDtoResponse> userToDtoList(List<User> users);


}

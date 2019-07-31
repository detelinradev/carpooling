package com.telerik.carpooling.models.dtos.dtos.mapper;

import com.telerik.carpooling.models.Car;
import com.telerik.carpooling.models.Comment;
import com.telerik.carpooling.models.Trip;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = { TripMapperResolver.class,CarMapperResolver.class,UserMapperResolver.class })
public interface DtoMapper {

    TripDtoResponse objectToDto(Trip trip);

    Trip dtoToObject(TripDtoEdit tripDtoEdit);

    Trip dtoToObject(TripDtoRequest tripRequestDto);

    Trip dtoToObject(TripDtoResponse tripResponseDto);

    CarDtoResponse objectToDto(Car car);

    Car dtoToObject(CarDtoRequest carDtoRequest);

    Car dtoToObject(CarDtoResponse carDtoResponse);

    UserDtoResponse objectToDto(User user);

    User dtoToObject(UserDtoRequest userDtoRequest);

    User dtoToObject(UserDtoResponse userDtoResponse);

    CommentDtoResponse objectToDto(Comment comment);

    Comment dtoToObject(CommentDtoRequest commentDtoRequest);

    Comment dtoToObject(CommentDtoResponse commentDtoResponse);

    Set<UserDtoResponse> usersToUserDtoResponses(Set<User> passengers);

    Set<CommentDtoResponse> commentsToCommentsDtoResponses(Set<Comment>comments);




}

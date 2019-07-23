package com.telerik.carpooling.models.dtos.dtos.mapper;

import com.telerik.carpooling.models.Car;
import com.telerik.carpooling.models.Comment;
import com.telerik.carpooling.models.Trip;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.*;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { TripMapperResolver.class,CarMapperResolver.class })
public interface DtoMapper {
    TripDtoResponse objectToDto(Trip trip);

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




}

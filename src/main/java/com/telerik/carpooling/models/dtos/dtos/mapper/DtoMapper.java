package com.telerik.carpooling.models.dtos.dtos.mapper;

import com.telerik.carpooling.models.Car;
import com.telerik.carpooling.models.Trip;
import com.telerik.carpooling.models.dtos.CarDtoRequest;
import com.telerik.carpooling.models.dtos.CarDtoResponse;
import com.telerik.carpooling.models.dtos.TripDtoRequest;
import com.telerik.carpooling.models.dtos.TripDtoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { TripMapperResolver.class })
public interface DtoMapper {
    TripDtoResponse objectToDto(Trip trip);

    Trip dtoToObject(TripDtoRequest tripRequestDto);

    Trip dtoToObject(TripDtoResponse tripResponseDto);

    Car dtoToObject(CarDtoRequest carDtoRequest);

    Car dtoToObject(CarDtoResponse carDtoResponse);

    CarDtoResponse objectToDto(Car car);


}

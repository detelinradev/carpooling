package com.telerik.carpooling.models.dtos.dtos.mapper;

import com.telerik.carpooling.models.Car;
import com.telerik.carpooling.models.Trip;
import com.telerik.carpooling.models.dtos.CarDto;
import com.telerik.carpooling.models.dtos.TripDtoRequest;
import com.telerik.carpooling.models.dtos.TripDtoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring")
public interface DtoMapper {
    TripDtoResponse objectToDto(Trip trip);
    Trip dtoToObject(TripDtoRequest tripRequestDto);
    Trip dtoToObject(TripDtoResponse tripResponseDto);
    CarDto objectToDto(Car car);
    Car dtoToObject(CarDto carDto);



}

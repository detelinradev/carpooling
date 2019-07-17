package com.telerik.carpooling.models.dtos;

import com.telerik.carpooling.models.Car;
import com.telerik.carpooling.models.Trip;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DtoMapper {
    TripDto objectToDto(Trip trip);
    Trip dtoToObject(TripDto tripDto);
    CarDto objectToDto(Car car);
    Car dtoToObject(CarDto carDto);


}

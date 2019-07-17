package com.telerik.carpooling.models.dtos;

import com.telerik.carpooling.models.Car;
import com.telerik.carpooling.models.Trip;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DtoMapper {
    TripDto objectToDto(Trip trip);
    Trip dtoToObject(TripDto tripDto);
    CarDto objectToDto(Car car);
    Car dtoToObject(CarDto carDto);


}

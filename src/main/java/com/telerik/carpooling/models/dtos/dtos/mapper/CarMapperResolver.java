package com.telerik.carpooling.models.dtos.dtos.mapper;


import com.telerik.carpooling.models.Car;
import com.telerik.carpooling.models.dtos.CarDtoResponse;
import com.telerik.carpooling.repositories.CarRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.ObjectFactory;
import org.mapstruct.TargetType;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CarMapperResolver {

    private final CarRepository carRepository;

    @ObjectFactory
    Car resolve(CarDtoResponse dto, @TargetType Class<Car> type) {
        return dto != null && dto.getId() != 0 ? carRepository.findById(dto.getId()).orElse(new Car()) : new Car();
    }
}

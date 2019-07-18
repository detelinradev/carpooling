package com.telerik.carpooling.models.dtos.dtos.mapper;

import com.telerik.carpooling.models.Trip;
import com.telerik.carpooling.models.dtos.TripDtoResponse;
import com.telerik.carpooling.repositories.TripRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.ObjectFactory;
import org.mapstruct.TargetType;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TripMapperResolver {

    private final TripRepository tripRepository;

    @ObjectFactory
    Trip resolve(TripDtoResponse dto, @TargetType Class<Trip> type) {
        return dto != null && dto.getId() != 0 ? tripRepository.findById(dto.getId()).orElse(new Trip()) : new Trip();
    }
}

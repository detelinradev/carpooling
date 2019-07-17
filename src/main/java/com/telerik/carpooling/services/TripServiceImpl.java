package com.telerik.carpooling.services;

import com.telerik.carpooling.models.Trip;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.dtos.mapper.DtoMapper;
import com.telerik.carpooling.models.dtos.TripDto;
import com.telerik.carpooling.repositories.TripRepository;
import com.telerik.carpooling.services.services.contracts.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;
    private final DtoMapper dtoMapper;

    @Override
    public TripDto createTrip(TripDto tripDto, User driver) {

        Trip trip = dtoMapper.dtoToObject(tripDto);
        trip.setDriver(driver);

        tripRepository.save(trip);
        return dtoMapper.objectToDto(trip);
    }

    @Override
    public TripDto updateTrip(Trip trip, User driver) {
        trip.setDriver(driver);
        tripRepository.save(trip);
        return dtoMapper.objectToDto(trip);
    }


}

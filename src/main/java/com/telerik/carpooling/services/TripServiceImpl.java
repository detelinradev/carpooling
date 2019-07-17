package com.telerik.carpooling.services;

import com.telerik.carpooling.models.Trip;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.DtoMapper;
import com.telerik.carpooling.models.dtos.TripDto;
import com.telerik.carpooling.repositories.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;
    private final DtoMapper dtoMapper;

    @Override
    public TripDto createTrip(TripDto tripDto, User driver) {

        Trip trip = dtoMapper.dtoToObject(tripDto);
        trip.setDriver(driver);

//        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//        Date date = new Date();
//        LocalDateTime dateTime = LocalDateTime.parse(dateFormat.format(date));
//        trip.setDriver(driver);
//        trip.setCreated(dateTime);
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

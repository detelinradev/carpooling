package com.telerik.carpooling.services;

import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.TripDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TripServiceImpl implements TripService {

    @Override
    public TripDto createTrip(TripDto trip, User driver) {

        return null;
    }


}

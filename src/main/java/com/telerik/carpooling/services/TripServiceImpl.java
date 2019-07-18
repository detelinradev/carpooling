package com.telerik.carpooling.services;

import com.telerik.carpooling.models.Trip;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.TripDtoResponse;
import com.telerik.carpooling.models.dtos.dtos.mapper.DtoMapper;
import com.telerik.carpooling.models.dtos.TripDtoRequest;
import com.telerik.carpooling.repositories.TripRepository;
import com.telerik.carpooling.repositories.UserRepository;
import com.telerik.carpooling.services.services.contracts.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final DtoMapper dtoMapper;

    @Override
    public TripDtoResponse createTrip(TripDtoRequest tripRequestDto, User driver) {

        Trip trip = dtoMapper.dtoToObject(tripRequestDto);
        trip.setDriver(driver);

        return dtoMapper.objectToDto(tripRepository.save(trip));
    }

    @Override
    public TripDtoResponse updateTrip(TripDtoResponse tripDtoResponse) {

        return dtoMapper.objectToDto(tripRepository.save(dtoMapper.dtoToObject(tripDtoResponse)));
    }

    @Override
    public Optional<Trip> tripRate(TripDtoResponse tripResponseDto, String userRole,
                                   int ratedUserID, String ratedUserRole, int rating) {
        Optional<Trip> trip = tripRepository.findById(tripResponseDto.getId());
        Optional<User> ratedUser = userRepository.findById(ratedUserID);

        if (trip.isPresent() && ratedUser.isPresent()) {
            if (userRole.equals("driver") && ratedUserRole.equals("passenger")) {
                if (trip.get().getPassengersAvailableForRate().contains(ratedUser.get())) {
                    trip.get().getPassengersAvailableForRate().remove(ratedUser.get());
                    int countRatings = ratedUser.get().getCountRatingsAsPassenger();
                    double averageRate = ratedUser.get().getAverageRatingPassenger();
                    averageRate = (averageRate * countRatings + rating) / (countRatings + 1);
                    ratedUser.get().setAverageRatingPassenger(averageRate);
                    ratedUser.get().setCountRatingsAsPassenger(countRatings + 1);
                }

            } else if (userRole.equals("passenger") && ratedUserRole.equals("driver")) {
                if (trip.get().getPassengersAllowedToRate().contains(ratedUser.get())) {
                    trip.get().getPassengersAllowedToRate().remove(ratedUser.get());
                        int countRatings = ratedUser.get().getCountRatingsAsDriver();
                        double averageRate = ratedUser.get().getAverageRatingDriver();
                        averageRate = (averageRate * countRatings + rating) / (countRatings + 1);
                        ratedUser.get().setAverageRatingDriver(averageRate);
                        ratedUser.get().setCountRatingsAsDriver(countRatings + 1);
                }
            }
        }
        return trip;
    }
}

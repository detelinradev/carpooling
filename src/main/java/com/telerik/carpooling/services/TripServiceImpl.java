package com.telerik.carpooling.services;

import com.telerik.carpooling.enums.PassengerStatus;
import com.telerik.carpooling.enums.TripStatus;
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

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;
    private final DtoMapper dtoMapper;

    @Override
    public TripDtoResponse createTrip(TripDtoRequest tripRequestDto, User driver) {

        Trip trip = dtoMapper.dtoToObject(tripRequestDto);
        trip.setDriver(driver);
        trip.setTripStatus(TripStatus.AVAILABLE);


        return dtoMapper.objectToDto(tripRepository.save(trip));
    }

    @Override
    public TripDtoResponse updateTrip(TripDtoResponse tripDtoResponse) {

        return dtoMapper.objectToDto(tripRepository.save(dtoMapper.dtoToObject(tripDtoResponse)));
    }

    public TripDtoResponse changeTripStatus(TripDtoResponse tripDtoResponse, User user, TripStatus tripStatus) {
        Optional<Trip> trip = tripRepository.findById(tripDtoResponse.getId());

        if (trip.isPresent() && trip.get().getDriver().equals(user)) {
            if (tripStatus.equals(TripStatus.BOOKED)
                    && trip.get().getTripStatus().equals(TripStatus.AVAILABLE))
                return tripMarkedAsBooked(tripDtoResponse);
            else if (tripStatus.equals(TripStatus.ONGOING)
                    && (trip.get().getTripStatus().equals(TripStatus.AVAILABLE)
                    || trip.get().getTripStatus().equals(TripStatus.BOOKED)))
                return tripMarkedAsOngoing(tripDtoResponse);
            else if (tripStatus.equals(TripStatus.DONE)
                    && trip.get().getTripStatus().equals(TripStatus.ONGOING))
                return tripMarkedAsDone(tripDtoResponse);
            else if (tripStatus.equals(TripStatus.CANCELED)
                    && !trip.get().getTripStatus().equals(TripStatus.DONE))
                return tripMarkedAsCanceled(tripDtoResponse);
        }
        return null;
    }

    public TripDtoResponse addPassenger(TripDtoResponse tripDtoResponse, User passenger) {
        Optional<Trip> trip = tripRepository.findById(tripDtoResponse.getId());

        if (trip.isPresent()) {
            if (trip.get().getTripStatus().equals(TripStatus.AVAILABLE)) {
                trip.get().getPassengerStatus().put(passenger, PassengerStatus.PENDING);
                return dtoMapper.objectToDto(tripRepository.save(trip.get()));
            }
        }

        return null;
    }

    public TripDtoResponse changePassengerStatus(TripDtoResponse tripDtoResponse, User user, PassengerStatus statusPassenger) {
        Optional<Trip> trip = tripRepository.findById(tripDtoResponse.getId());

        if (trip.isPresent()) {
            if (trip.get().getPassengerStatus().keySet().contains(user)) {
                if (statusPassenger.equals(PassengerStatus.CANCELED)) {
                    return cancelPassenger(user, tripDtoResponse);
                }
            } else if (trip.get().getDriver().equals(user)) {
                if (statusPassenger.equals(PassengerStatus.ACCEPTED)
                        && trip.get().getTripStatus().equals(TripStatus.AVAILABLE)) {
                    return acceptPassenger(user, tripDtoResponse);
                } else if (statusPassenger.equals(PassengerStatus.REJECTED)
                        && (trip.get().getTripStatus().equals(TripStatus.AVAILABLE)
                        || trip.get().getTripStatus().equals(TripStatus.BOOKED))) {
                    return rejectPassenger(user, tripDtoResponse);
                } else if (statusPassenger.equals(PassengerStatus.ABSENT)
                        && (trip.get().getTripStatus().equals(TripStatus.AVAILABLE)
                        || trip.get().getTripStatus().equals(TripStatus.BOOKED))
                        && user.getPassengerStatus().equals(PassengerStatus.ACCEPTED)) {
                    return absentPassenger(tripDtoResponse, user);
                }
            }
        }

        return null;
    }

    private TripDtoResponse cancelPassenger(User user, TripDtoResponse tripDtoResponse) {
        Optional<Trip> trip = tripRepository.findById(tripDtoResponse.getId());

        if (trip.isPresent()) {
            if (trip.get().getPassengerStatus().get(user).equals(PassengerStatus.PENDING)) {
                trip.get().getPassengerStatus().put(user, PassengerStatus.CANCELED);
                return dtoMapper.objectToDto(tripRepository.save(trip.get()));
            } else if (trip.get().getPassengerStatus().get(user).equals(PassengerStatus.ACCEPTED)) {
                trip.get().getPassengerStatus().put(user, PassengerStatus.CANCELED);
                trip.get().getPassengersAllowedToRate().remove(user);
                if (trip.get().getTripStatus().equals(TripStatus.BOOKED)) {
                    trip.get().setTripStatus(TripStatus.AVAILABLE);
                }
                return dtoMapper.objectToDto(tripRepository.save(trip.get()));
            }
        }
        return null;
    }

    private TripDtoResponse rejectPassenger(User user, TripDtoResponse tripDtoResponse) {
        Optional<Trip> trip = tripRepository.findById(tripDtoResponse.getId());

        if (trip.isPresent()) {
            if (user.getPassengerStatus().equals(PassengerStatus.ACCEPTED)) {
                trip.get().getPassengerStatus().put(user, PassengerStatus.REJECTED);
                trip.get().getPassengersAvailableForRate().remove(user);
                if (trip.get().getTripStatus().equals(TripStatus.BOOKED)) {
                    trip.get().setTripStatus(TripStatus.AVAILABLE);
                }
                return dtoMapper.objectToDto(tripRepository.save(trip.get()));
            } else if (user.getPassengerStatus().equals(PassengerStatus.PENDING)) {
                trip.get().getPassengerStatus().put(user, PassengerStatus.REJECTED);
                return dtoMapper.objectToDto(tripRepository.save(trip.get()));
            }
        }
        return null;
    }

    private TripDtoResponse acceptPassenger(User user, TripDtoResponse tripDtoResponse) {
        Optional<Trip> trip = tripRepository.findById(tripDtoResponse.getId());

        if (trip.isPresent()) {
            trip.get().getPassengerStatus().put(user, PassengerStatus.ACCEPTED);
            trip.get().getPassengersAllowedToRate().add(user);
            trip.get().getPassengersAvailableForRate().add(user);
            if (trip.get().getDriver().getCar().getTotalSeats()
                    == trip.get().getPassengerStatus().values()
                    .stream()
                    .filter(k -> k.equals(PassengerStatus.ACCEPTED))
                    .count()) {
                trip.get().setTripStatus(TripStatus.BOOKED);
            }
            return dtoMapper.objectToDto(tripRepository.save(trip.get()));
        }
        return null;
    }

    private TripDtoResponse absentPassenger(TripDtoResponse tripDtoResponse, User user) {
        Optional<Trip> trip = tripRepository.findById(tripDtoResponse.getId());

        if (trip.isPresent()) {
            trip.get().getPassengerStatus().put(user, PassengerStatus.ABSENT);
            trip.get().getPassengersAllowedToRate().remove(user);
            if (trip.get().getTripStatus().equals(TripStatus.BOOKED))
                trip.get().setTripStatus(TripStatus.AVAILABLE);

            return dtoMapper.objectToDto(tripRepository.save(trip.get()));
        }
        return null;
    }

    private TripDtoResponse tripMarkedAsBooked(TripDtoResponse tripDtoResponse) {
        Optional<Trip> trip = tripRepository.findById(tripDtoResponse.getId());

        if (trip.isPresent()) {
            trip.get().setTripStatus(TripStatus.BOOKED);
            return dtoMapper.objectToDto(tripRepository.save(trip.get()));
        }
        return null;
    }

    private TripDtoResponse tripMarkedAsOngoing(TripDtoResponse tripDtoResponse) {
        Optional<Trip> trip = tripRepository.findById(tripDtoResponse.getId());

        if (trip.isPresent()) {
            trip.get().setTripStatus(TripStatus.ONGOING);
            return dtoMapper.objectToDto(tripRepository.save(trip.get()));
        }
        return null;
    }

    private TripDtoResponse tripMarkedAsDone(TripDtoResponse tripDtoResponse) {
        Optional<Trip> trip = tripRepository.findById(tripDtoResponse.getId());

        if (trip.isPresent()) {
            trip.get().setTripStatus(TripStatus.DONE);
            return dtoMapper.objectToDto(tripRepository.save(trip.get()));
        }
        return null;
    }

    private TripDtoResponse tripMarkedAsCanceled(TripDtoResponse tripDtoResponse) {
        Optional<Trip> trip = tripRepository.findById(tripDtoResponse.getId());

        if (trip.isPresent()) {
            trip.get().setTripStatus(TripStatus.CANCELED);
            return dtoMapper.objectToDto(tripRepository.save(trip.get()));
        }
        return null;
    }

}

package com.telerik.carpooling.services;

import com.telerik.carpooling.enums.PassengerStatus;
import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.models.Trip;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.TripDtoEdit;
import com.telerik.carpooling.models.dtos.TripDtoResponse;
import com.telerik.carpooling.models.dtos.dtos.mapper.DtoMapper;
import com.telerik.carpooling.models.dtos.TripDtoRequest;
import com.telerik.carpooling.repositories.TripRepository;
import com.telerik.carpooling.repositories.UserRepository;
import com.telerik.carpooling.services.services.contracts.TripService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.security.InvalidParameterException;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Log4j2
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final DtoMapper dtoMapper;

    @Override
    public Trip createTrip(TripDtoRequest tripRequestDto, User driver) {

        if (driver.getCar() != null) {
            Trip trip = dtoMapper.dtoToObject(tripRequestDto);
            trip.setDriver(driver);
            trip.setCar(driver.getCar());
            trip.setTripStatus(TripStatus.AVAILABLE);
            return tripRepository.save(trip);
        }
        return null;
    }

    @Override
    public Trip updateTrip(TripDtoEdit tripDtoEdit) {

        return tripRepository.save(dtoMapper.dtoToObject(tripDtoEdit));
    }

    @Override
    public TripDtoResponse getTrip(String tripID) {

        long intTripID = parseStringToLong(tripID);
        Optional<Trip> trip = tripRepository.findById(intTripID);
        return trip.map(value -> dtoMapper.objectToDto(tripRepository.save(value))).orElse(null);
    }

    @Override
    public List<TripDtoResponse> getTrips(Integer pageNumber, Integer pageSize, String tripStatus, String driverUsername,
                                          String origin, String destination, String earliestDepartureTime,
                                          String latestDepartureTime, String availablePlaces, String smoking,
                                          String pets, String luggage) {

        if ((smoking == null || (smoking.equalsIgnoreCase("yes") || smoking.equalsIgnoreCase("no"))) &&
                (pets == null || (pets.equalsIgnoreCase("yes") || pets.equalsIgnoreCase("no"))) &&
                (luggage == null || (luggage.equalsIgnoreCase("yes") || luggage.equalsIgnoreCase("no"))) &&
                (availablePlaces == null || (parseStringToLong(availablePlaces) > 0 && parseStringToLong(availablePlaces) < 9)) &&
                (tripStatus == null || (tripStatus.equalsIgnoreCase(
                        Arrays.stream(TripStatus.values())
                                .filter(k -> k.toString().equalsIgnoreCase(tripStatus))
                                .findAny()
                                .map(TripStatus::getCode)
                                .orElse("")))) &&
                ((driverUsername == null) || (userRepository.findFirstByUsername(driverUsername) != null)) &&
                ((pageNumber != null && pageSize != null) || (pageNumber == null && pageSize == null) )) {

            return dtoMapper.tripToDtoList(
                    tripRepository.findTripsByPassedParameters(
                            Arrays.stream(TripStatus.values())
                                    .filter(k -> k.toString().equalsIgnoreCase(tripStatus))
                                    .findAny()
                                    .orElse(null),
                            userRepository.findFirstByUsername(driverUsername),
                            origin, destination, earliestDepartureTime,
                            (parseStringToLong(availablePlaces) != null ? parseStringToLong(availablePlaces).intValue() : null),
                            smoking, pets, luggage, (pageNumber != null ? PageRequest.of(pageNumber, pageSize) : null)));
        } else return null;
    }

    @Override
    public Trip changeTripStatus(String tripID, User user, TripStatus tripStatus) {

        long intTripID = parseStringToLong(tripID);

        Optional<Trip> trip = tripRepository.findById(intTripID);

        if (trip.isPresent() && trip.get().getDriver().equals(user)) {
            if (tripStatus.equals(TripStatus.BOOKED)
                    && trip.get().getTripStatus().equals(TripStatus.AVAILABLE))
                return markTripAsBooked(intTripID);
            else if (tripStatus.equals(TripStatus.ONGOING)
                    && (trip.get().getTripStatus().equals(TripStatus.AVAILABLE)
                    || trip.get().getTripStatus().equals(TripStatus.BOOKED)))
                return markTripAsOngoing(intTripID);
            else if (tripStatus.equals(TripStatus.DONE)
                    && trip.get().getTripStatus().equals(TripStatus.ONGOING))
                return markTripAsDone(intTripID);
            else if (tripStatus.equals(TripStatus.CANCELED)
                    && !trip.get().getTripStatus().equals(TripStatus.DONE))
                return markTripAsCanceled(intTripID);
        }
        return null;
    }

    @Override
    public Trip addPassenger(String tripID, User passenger) {

        long intTripID = parseStringToLong(tripID);

        Optional<Trip> trip = tripRepository.findById(intTripID);
        if (trip.isPresent()) {
            if (trip.get().getTripStatus().equals(TripStatus.AVAILABLE)
                    && !trip.get().getDriver().equals(passenger)) {
                trip.get().getPassengerStatus().put(passenger, PassengerStatus.PENDING);
                return tripRepository.save(trip.get());
            }
        }

        return null;
    }

    public Trip changePassengerStatus(String tripID, User user, String passengerID, PassengerStatus statusPassenger) {

        long intTripID = parseStringToLong(tripID);
        long intPassengerID = parseStringToLong(passengerID);

        Optional<Trip> trip = tripRepository.findById(intTripID);
        Optional<User> passenger = userRepository.findById(intPassengerID);

        if (trip.isPresent() && passenger.isPresent()) {
            if (trip.get().getPassengerStatus().keySet().stream().anyMatch(k -> k.equals(user))) {
                if (statusPassenger.equals(PassengerStatus.CANCELED)) {
                    return cancelPassenger(user, intTripID);
                }
            }
            if (trip.get().getDriver().equals(user)
                    && trip.get().getPassengerStatus().keySet().stream().anyMatch(k -> k.equals(passenger.get()))) {
                if (statusPassenger.equals(PassengerStatus.ACCEPTED) &&
                        trip.get().getTripStatus().equals(TripStatus.AVAILABLE)) {
                    return acceptPassenger(intPassengerID, intTripID);
                } else if (statusPassenger.equals(PassengerStatus.REJECTED) &&
                        (trip.get().getTripStatus().equals(TripStatus.AVAILABLE) ||
                                trip.get().getTripStatus().equals(TripStatus.BOOKED))) {
                    return rejectPassenger(intPassengerID, intTripID);
                } else if (statusPassenger.equals(PassengerStatus.ABSENT) &&
                        (trip.get().getTripStatus().equals(TripStatus.AVAILABLE) ||
                                trip.get().getTripStatus().equals(TripStatus.BOOKED)) &&
                        user.getPassengerStatus().equals(PassengerStatus.ACCEPTED)) {
                    return absentPassenger(intTripID, intPassengerID);
                }
            }
        }
        return null;
    }

    private Trip cancelPassenger(User user, Long tripID) {
        Optional<Trip> trip = tripRepository.findById(tripID);

        if (trip.isPresent()) {

            if (trip.get().getPassengerStatus().get(user).equals(PassengerStatus.PENDING)) {
                trip.get().getPassengerStatus().put(user, PassengerStatus.CANCELED);
                return tripRepository.save(trip.get());
            } else if (trip.get().getPassengerStatus().get(user).equals(PassengerStatus.ACCEPTED)) {
                trip.get().getPassengerStatus().put(user, PassengerStatus.CANCELED);
                trip.get().getPassengersAllowedToRate().remove(user);
                trip.get().getPassengersAllowedToGiveFeedback().remove(user);
                trip.get().getPassengers().remove(user);

                if (trip.get().getTripStatus().equals(TripStatus.BOOKED)) {
                    trip.get().setTripStatus(TripStatus.AVAILABLE);
                }

                return tripRepository.save(trip.get());
            }
        }
        return null;
    }

    private Trip rejectPassenger(Long passengerID, Long tripID) {
        Optional<Trip> trip = tripRepository.findById(tripID);
        Optional<User> passenger = userRepository.findById(passengerID);
        if (trip.isPresent() && passenger.isPresent()) {
            if (trip.get().getPassengerStatus().get(passenger.get()).equals(PassengerStatus.ACCEPTED)) {
                trip.get().getPassengerStatus().put(passenger.get(), PassengerStatus.REJECTED);
                trip.get().getPassengersAvailableForRate().remove(passenger.get());
                trip.get().getPassengersAvailableForFeedback().remove(passenger.get());
                trip.get().getPassengers().remove(passenger.get());
                if (trip.get().getTripStatus().equals(TripStatus.BOOKED)) {
                    trip.get().setTripStatus(TripStatus.AVAILABLE);
                }
                return tripRepository.save(trip.get());
            } else if (trip.get().getPassengerStatus().get(passenger.get()).equals(PassengerStatus.PENDING)) {
                trip.get().getPassengerStatus().put(passenger.get(), PassengerStatus.REJECTED);
                return tripRepository.save(trip.get());
            }
        }
        return null;
    }

    private Trip acceptPassenger(Long passengerID, Long tripID) {
        Optional<Trip> trip = tripRepository.findById(tripID);
        Optional<User> passenger = userRepository.findById(passengerID);
        if (trip.isPresent() && passenger.isPresent()) {
            trip.get().getPassengerStatus().put(passenger.get(), PassengerStatus.ACCEPTED);
            trip.get().getPassengersAllowedToRate().add(passenger.get());
            trip.get().getPassengersAvailableForRate().add(passenger.get());
            trip.get().getPassengersAllowedToGiveFeedback().add(passenger.get());
            trip.get().getPassengersAvailableForFeedback().add(passenger.get());
            trip.get().getPassengers().add(passenger.get());
            if (trip.get().getAvailablePlaces()
                    == trip.get().getPassengerStatus().values()
                    .stream()
                    .filter(k -> k.equals(PassengerStatus.ACCEPTED))
                    .count()) {
                trip.get().setTripStatus(TripStatus.BOOKED);
            }
            return tripRepository.save(trip.get());
        }
        return null;
    }

    private Trip absentPassenger(Long tripID, Long passengerID) {
        Optional<Trip> trip = tripRepository.findById(tripID);
        Optional<User> passenger = userRepository.findById(passengerID);

        if (trip.isPresent() && passenger.isPresent()) {
            trip.get().getPassengerStatus().put(passenger.get(), PassengerStatus.ABSENT);
            trip.get().getPassengersAllowedToRate().remove(passenger.get());
            trip.get().getPassengersAllowedToGiveFeedback().remove(passenger.get());
            trip.get().getPassengers().remove(passenger.get());
            if (trip.get().getTripStatus().equals(TripStatus.BOOKED))
                trip.get().setTripStatus(TripStatus.AVAILABLE);

            return tripRepository.save(trip.get());
        }
        return null;
    }

    private Trip markTripAsBooked(Long tripID) {
        Optional<Trip> trip = tripRepository.findById(tripID);

        if (trip.isPresent()) {
            trip.get().setTripStatus(TripStatus.BOOKED);
            trip.get().getPassengerStatus().values()
                    .stream()
                    .filter(k -> k.equals(PassengerStatus.PENDING))
                    .forEach(p -> p = PassengerStatus.REJECTED);
            return tripRepository.save(trip.get());
        }
        return null;
    }

    private Trip markTripAsOngoing(Long tripID) {
        Optional<Trip> trip = tripRepository.findById(tripID);

        if (trip.isPresent()) {
            trip.get().setTripStatus(TripStatus.ONGOING);
            return tripRepository.save(trip.get());
        }
        return null;
    }

    private Trip markTripAsDone(Long tripID) {
        Optional<Trip> trip = tripRepository.findById(tripID);

        if (trip.isPresent()) {
            trip.get().setTripStatus(TripStatus.DONE);
            return tripRepository.save(trip.get());
        }
        return null;
    }

    private Trip markTripAsCanceled(Long tripID) {
        Optional<Trip> trip = tripRepository.findById(tripID);

        trip.ifPresent(value -> value.setTripStatus(TripStatus.CANCELED));
        return null;
    }

    private Long parseStringToLong(String stringID) {
        if (stringID != null) {
            long longID = 0;
            try {
                longID = Long.parseLong(stringID);
            } catch (NumberFormatException e) {
                log.error("Exception during parsing", e);
            }
            return longID;
        }
        return null;
    }

}

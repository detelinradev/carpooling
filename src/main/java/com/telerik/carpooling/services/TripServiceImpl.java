package com.telerik.carpooling.services;

import com.telerik.carpooling.enums.UserStatus;
import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.models.Trip;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.repositories.TripRepository;
import com.telerik.carpooling.repositories.UserRepository;
import com.telerik.carpooling.services.services.contracts.TripService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Log4j2
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;
    private final UserRepository userRepository;

    @Override
    public Trip createTrip(Trip trip, User driver) {

        if (driver.getCar() != null) {
            trip.setCar(driver.getCar());
            trip.setTripStatus(TripStatus.AVAILABLE);
            trip.getUserStatus().put(driver,UserStatus.DRIVER);
            tripRepository.save(trip);
            driver.getMyTrips().put(trip,UserStatus.DRIVER);
            userRepository.save(driver);
            return trip;
        }
        return null;
    }

    @Override
    public Trip updateTrip(Trip trip) {

        return tripRepository.save(trip);
    }

    @Override
    public Trip getTrip(String tripID) {

        long longTripID = parseStringToLong(tripID);
        Optional<Trip> trip = tripRepository.findByModelIdAndIsDeleted(longTripID);
        return trip.map(tripRepository::save).orElse(null);
    }

    @Override
    public List<Trip> getTrips(Integer pageNumber, Integer pageSize, String tripStatus, String driverUsername,
                                          String origin, String destination, String earliestDepartureTime,
                                          String latestDepartureTime, String availablePlaces, String smoking,
                                          String pets, String luggage,String airConditioned)  {

        if ((smoking == null || (smoking.equalsIgnoreCase("yes") || smoking.equalsIgnoreCase("no"))) &&
                (pets == null || (pets.equalsIgnoreCase("yes") || pets.equalsIgnoreCase("no"))) &&
                (airConditioned == null || (airConditioned.equalsIgnoreCase("yes") || airConditioned.equalsIgnoreCase("no"))) &&
                (luggage == null || (luggage.equalsIgnoreCase("yes") || luggage.equalsIgnoreCase("no"))) &&
                (availablePlaces == null || (parseStringToLong(availablePlaces) > 0 && parseStringToLong(availablePlaces) < 9)) &&
                (tripStatus == null || (tripStatus.equalsIgnoreCase(
                        Arrays.stream(TripStatus.values())
                                .filter(k -> k.toString().equalsIgnoreCase(tripStatus))
                                .findAny()
                                .map(TripStatus::getCode)
                                .orElse("")))) &&
                ((driverUsername == null) || (userRepository.findFirstByUsername(driverUsername) != null)) &&
                ((pageNumber != null && pageSize != null) || (pageNumber == null && pageSize == null))) {
            return
                    tripRepository.findTripsByPassedParameters(
                            Arrays.stream(TripStatus.values())
                                    .filter(k -> k.toString().equalsIgnoreCase(tripStatus))
                                    .findAny()
                                    .orElse(null),
                            userRepository.findFirstByUsername(driverUsername),
                            origin, destination, parseDateTime(earliestDepartureTime),parseDateTime(latestDepartureTime),
                            (parseStringToLong(availablePlaces) != null ? parseStringToLong(availablePlaces).intValue() : null),
                            smoking, pets, luggage,airConditioned, (pageNumber != null ? PageRequest.of(pageNumber, pageSize) : null));
        }
        else return null;
    }

    @Override
    public Trip deleteTrip(String tripId, User user) {

        long tripID = parseStringToLong(tripId);


        Optional<Trip> trip = tripRepository.findByModelIdAndIsDeleted(tripID);
        System.out.println(trip.isPresent());
        if(trip.isPresent()) {
            if (trip.get().getUserStatus().containsKey(user))
                if(trip.get().getUserStatus().get(user).equals(UserStatus.DRIVER))
                trip.get().setIsDeleted(true);
            return tripRepository.save(trip.get());
        }
        return null;
    }

    @Override
    public Trip changeTripStatus(String tripID, User user, TripStatus tripStatus) {

        long intTripID = parseStringToLong(tripID);

        Optional<Trip> trip = tripRepository.findByModelIdAndIsDeleted(intTripID);

        if (trip.isPresent() && trip.get().getUserStatus().containsKey(user)) {
            if(trip.get().getUserStatus().get(user).equals(UserStatus.DRIVER)) {
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
        }
        return null;
    }

    @Override
    public Trip addPassenger(String tripID, User passenger) {

        long intTripID = parseStringToLong(tripID);
        Optional<Trip> trip = tripRepository.findByModelIdAndIsDeleted(intTripID);
        if (trip.isPresent()) {
            if (trip.get().getTripStatus().equals(TripStatus.AVAILABLE)
                    && !trip.get().getUserStatus().containsKey(passenger)) {
                trip.get().getUserStatus().put(passenger, UserStatus.PENDING);
                tripRepository.save(trip.get());
                userRepository.save(passenger);
                return trip.get();
            }
        }

        return null;
    }

    public Trip changePassengerStatus(String tripID, User user, String passengerID, UserStatus statusPassenger) {

        long intTripID = parseStringToLong(tripID);
        long intPassengerID = parseStringToLong(passengerID);

        Optional<Trip> trip = tripRepository.findByModelIdAndIsDeleted(intTripID);
        Optional<User> passenger = userRepository.findById(intPassengerID);

        if (trip.isPresent() && passenger.isPresent()) {
            if (trip.get().getUserStatus().keySet().stream().anyMatch(k -> k.equals(user))) {
                if (statusPassenger.equals(UserStatus.CANCELED)) {
                    return cancelPassenger(user, intTripID);
                }
            }
            if (trip.get().getUserStatus().containsKey(user) && trip.get().getUserStatus().containsKey(passenger.get())) {
                if (trip.get().getUserStatus().get(user).equals(UserStatus.DRIVER)) {
                    if (statusPassenger.equals(UserStatus.ACCEPTED) &&
                            trip.get().getTripStatus().equals(TripStatus.AVAILABLE)) {
                        return acceptPassenger(intPassengerID, intTripID);
                    } else if (statusPassenger.equals(UserStatus.REJECTED)) {
                        return rejectPassenger(intPassengerID, intTripID);
                    } else if (statusPassenger.equals(UserStatus.ABSENT) &&
                            (trip.get().getTripStatus().equals(TripStatus.AVAILABLE) ||
                                    trip.get().getTripStatus().equals(TripStatus.BOOKED)) &&
                            user.getMyTrips().get(trip.get()).equals(UserStatus.ACCEPTED)) {
                        return absentPassenger(intTripID, intPassengerID);
                    }
                }
            }
        }
        return null;
    }

    private Trip cancelPassenger(User user, Long tripID) {
        Optional<Trip> trip = tripRepository.findByModelIdAndIsDeleted(tripID);

        if (trip.isPresent()) {

            if (trip.get().getUserStatus().get(user).equals(UserStatus.PENDING)) {
            } else if (trip.get().getUserStatus().get(user).equals(UserStatus.ACCEPTED)) {
                trip.get().setAvailablePlaces(trip.get().getAvailablePlaces() + 1);

                if (trip.get().getTripStatus().equals(TripStatus.BOOKED)) {
                    trip.get().setTripStatus(TripStatus.AVAILABLE);
                }
            }
            trip.get().getUserStatus().put(user, UserStatus.CANCELED);
            tripRepository.save(trip.get());
            user.getMyTrips().remove(trip.get());
            userRepository.save(user);
            return trip.get();
        }
        return null;
    }

    private Trip rejectPassenger(Long passengerID, Long tripID) {
        Optional<Trip> trip = tripRepository.findByModelIdAndIsDeleted(tripID);
        Optional<User> passenger = userRepository.findById(passengerID);
        if (trip.isPresent() && passenger.isPresent()) {
            if (trip.get().getUserStatus().get(passenger.get()).equals(UserStatus.ACCEPTED)) {
                trip.get().setAvailablePlaces(trip.get().getAvailablePlaces() + 1);
                if (trip.get().getTripStatus().equals(TripStatus.BOOKED)) {
                    trip.get().setTripStatus(TripStatus.AVAILABLE);
                }
            } else if (trip.get().getUserStatus().get(passenger.get()).equals(UserStatus.PENDING)) {
            }
            trip.get().getUserStatus().put(passenger.get(), UserStatus.REJECTED);
            return tripRepository.save(trip.get());
        }
        return null;
    }

    private Trip acceptPassenger(Long passengerID, Long tripID) {
        Optional<Trip> trip = tripRepository.findByModelIdAndIsDeleted(tripID);
        Optional<User> passenger = userRepository.findById(passengerID);
        if (trip.isPresent() && passenger.isPresent() && !trip.get().getUserStatus().get(passenger.get()).equals(UserStatus.CANCELED)) {
            trip.get().getUserStatus().put(passenger.get(), UserStatus.ACCEPTED);
            trip.get().setAvailablePlaces(trip.get().getAvailablePlaces() - 1);
            if (trip.get().getAvailablePlaces()
                    == trip.get().getUserStatus().values()
                    .stream()
                    .filter(k -> k.equals(UserStatus.ACCEPTED))
                    .count()) {
                trip.get().setTripStatus(TripStatus.BOOKED);
            }
            return tripRepository.save(trip.get());
        }
        return null;
    }

    private Trip absentPassenger(Long tripID, Long passengerID) {
        Optional<Trip> trip = tripRepository.findByModelIdAndIsDeleted(tripID);
        Optional<User> passenger = userRepository.findById(passengerID);

        if (trip.isPresent() && passenger.isPresent()) {
            trip.get().getUserStatus().put(passenger.get(), UserStatus.ABSENT);
            trip.get().setAvailablePlaces(trip.get().getAvailablePlaces() + 1);
            if (trip.get().getTripStatus().equals(TripStatus.BOOKED))
                trip.get().setTripStatus(TripStatus.AVAILABLE);

            return tripRepository.save(trip.get());
        }
        return null;
    }

    private Trip markTripAsBooked(Long tripID) {
        Optional<Trip> trip = tripRepository.findByModelIdAndIsDeleted(tripID);

        if (trip.isPresent()) {
            trip.get().setTripStatus(TripStatus.BOOKED);
            trip.get().getUserStatus().values()
                    .stream()
                    .filter(k -> k.equals(UserStatus.PENDING))
                    .forEach(p -> p = UserStatus.REJECTED);
            return tripRepository.save(trip.get());
        }
        return null;
    }

    private Trip markTripAsOngoing(Long tripID) {
        Optional<Trip> trip = tripRepository.findByModelIdAndIsDeleted(tripID);

        if (trip.isPresent()) {
            trip.get().setTripStatus(TripStatus.ONGOING);
            return tripRepository.save(trip.get());
        }
        return null;
    }

    private Trip markTripAsDone(Long tripID) {
        Optional<Trip> trip = tripRepository.findByModelIdAndIsDeleted(tripID);

        if (trip.isPresent()) {
            trip.get().setTripStatus(TripStatus.DONE);
            return tripRepository.save(trip.get());
        }
        return null;
    }

    private Trip markTripAsCanceled(Long tripID) {
        Optional<Trip> trip = tripRepository.findByModelIdAndIsDeleted(tripID);

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

    private LocalDateTime parseDateTime(String departureTime) {
        LocalDateTime departureTimeFormat;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        if(departureTime != null) {
            try {
                departureTimeFormat = LocalDateTime.parse(departureTime,dateTimeFormatter);
                return departureTimeFormat;
            } catch (Exception e) {
                log.error("Exception during parsing", e);
            }
        }
        return null;
    }

}

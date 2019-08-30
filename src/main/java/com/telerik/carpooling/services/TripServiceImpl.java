package com.telerik.carpooling.services;

import com.telerik.carpooling.enums.PassengerStatus;
import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.models.Trip;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.repositories.RatingRepository;
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
    private final RatingRepository ratingRepository;

    @Override
    public Trip createTrip(Trip trip, User driver) {

        if (driver.getCar() != null) {
            trip.setDriver(driver);
            trip.setCar(driver.getCar());
            trip.setTripStatus(TripStatus.AVAILABLE);
            trip.setIsDeleted(false);
            driver.getMyTrips().put(trip, PassengerStatus.DRIVER);
            tripRepository.save(trip);
            userRepository.save(driver);
            return trip;
        }
        return null;
    }

    @Override
    public Trip updateTrip(Trip trip, User user) {

        if (trip.getDriver().equals(user))
            return tripRepository.save(trip);
        return null;
    }

    @Override
    public Trip getTrip(String tripID) {

        long longTripID = parseStringToLong(tripID);
        Optional<Trip> trip = tripRepository.findByModelIdAndIsDeleted(longTripID);
        return trip.map(tripRepository::save).orElse(null);
    }

    @Override
    public List<Trip> getTrips(Integer pageNumber, Integer pageSize, String tripStatus,
                               String origin, String destination, String earliestDepartureTime,
                               String latestDepartureTime, String availablePlaces, String smoking,
                               String pets, String luggage, String airConditioned) {

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
                ((pageNumber != null && pageSize != null) || (pageNumber == null && pageSize == null))) {
            List<Trip> trips = tripRepository.findTripsByPassedParameters(
                    Arrays.stream(TripStatus.values())
                            .filter(k -> k.toString().equalsIgnoreCase(tripStatus))
                            .findAny()
                            .orElse(null),
                    origin, destination, parseDateTime(earliestDepartureTime), parseDateTime(latestDepartureTime),
                    (parseStringToLong(availablePlaces) != null ? parseStringToLong(availablePlaces).intValue() : null),
                    smoking, pets, luggage, (pageNumber != null ? PageRequest.of(pageNumber, pageSize) : null));
            trips.forEach(t -> t.getPassengerStatus().keySet()
                    .forEach(k -> k.setRatingAsPassenger(ratingRepository.findAverageRatingByUserAsPassenger(k.getModelId()))));
            return trips;
        } else return null;
    }

    @Override
    public Trip deleteTrip(String tripId, User user) {

        long tripID = parseStringToLong(tripId);


        Optional<Trip> trip = tripRepository.findByModelIdAndIsDeleted(tripID);
        if (trip.isPresent()) {
            if (trip.get().getDriver().equals(user))
                trip.get().setIsDeleted(true);
            return tripRepository.save(trip.get());
        }
        return null;
    }

    @Override
    public Trip changeTripStatus(String tripID, User user, TripStatus tripStatus) {

        long longTripID = parseStringToLong(tripID);

        Optional<Trip> trip = tripRepository.findByModelIdAndIsDeleted(longTripID);
        if (trip.isPresent() && trip.get().getDriver().equals(user)) {
            if (tripStatus.equals(TripStatus.BOOKED)
                    && trip.get().getTripStatus().equals(TripStatus.AVAILABLE))
                return markTripAsBooked(longTripID);
            else if (tripStatus.equals(TripStatus.ONGOING)
                    && (trip.get().getTripStatus().equals(TripStatus.AVAILABLE)
                    || trip.get().getTripStatus().equals(TripStatus.BOOKED)))
                return markTripAsOngoing(longTripID);
            else if (tripStatus.equals(TripStatus.DONE)
                    && trip.get().getTripStatus().equals(TripStatus.ONGOING))
                return markTripAsDone(longTripID);
            else if (tripStatus.equals(TripStatus.CANCELED)
                    && !trip.get().getTripStatus().equals(TripStatus.DONE))
                return markTripAsCanceled(longTripID);
        }
        return null;
    }

    @Override
    public Trip addPassenger(String tripID, User passenger) {

        long intTripID = parseStringToLong(tripID);
        Optional<Trip> trip = tripRepository.findByModelIdAndIsDeleted(intTripID);
        if (trip.isPresent()) {
            if (trip.get().getTripStatus().equals(TripStatus.AVAILABLE)
                    && !trip.get().getPassengerStatus().containsKey(passenger)) {
                trip.get().getPassengerStatus().put(passenger, PassengerStatus.PENDING);
                passenger.getMyTrips().put(trip.get(), PassengerStatus.PENDING);
                tripRepository.save(trip.get());
                userRepository.save(passenger);
                return trip.get();
            }
        }

        return null;
    }

    public Trip changePassengerStatus(String tripID, User user, String passengerID, PassengerStatus statusPassenger) {

        long intTripID = parseStringToLong(tripID);
        long intPassengerID = parseStringToLong(passengerID);

        Optional<Trip> trip = tripRepository.findByModelIdAndIsDeleted(intTripID);
        Optional<User> passenger = userRepository.findById(intPassengerID);

        if (trip.isPresent() && passenger.isPresent()) {
            if (trip.get().getPassengerStatus().containsKey(user)) {
                if (statusPassenger.equals(PassengerStatus.CANCELED)) {
                    return cancelPassenger(user, intTripID);
                }
            }
            if (trip.get().getDriver().equals(user)
                    && trip.get().getPassengerStatus().containsKey(passenger.get())) {
                if (statusPassenger.equals(PassengerStatus.ACCEPTED) &&
                        trip.get().getTripStatus().equals(TripStatus.AVAILABLE)
                        && trip.get().getPassengerStatus().get(passenger.get()).equals(PassengerStatus.PENDING)) {
                    return acceptPassenger(intPassengerID, intTripID);
                } else if (statusPassenger.equals(PassengerStatus.REJECTED)) {
                    return rejectPassenger(intPassengerID, intTripID);
                } else if (statusPassenger.equals(PassengerStatus.ABSENT) &&
                        (trip.get().getTripStatus().equals(TripStatus.AVAILABLE) ||
                                trip.get().getTripStatus().equals(TripStatus.BOOKED)) &&
                        user.getMyTrips().get(trip.get()).equals(PassengerStatus.ACCEPTED)) {
                    return absentPassenger(intTripID, intPassengerID);
                }
            }
        }
        return null;
    }

    private Trip cancelPassenger(User user, Long tripID) {
        Optional<Trip> trip = tripRepository.findByModelIdAndIsDeleted(tripID);

        if (trip.isPresent()) {

            if (trip.get().getPassengerStatus().get(user).equals(PassengerStatus.ACCEPTED)) {
                trip.get().setAvailablePlaces(trip.get().getAvailablePlaces() + 1);

                if (trip.get().getTripStatus().equals(TripStatus.BOOKED)) {
                    trip.get().setTripStatus(TripStatus.AVAILABLE);
                }
            }

            trip.get().getPassengerStatus().put(user, PassengerStatus.CANCELED);
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
            if (trip.get().getPassengerStatus().get(passenger.get()).equals(PassengerStatus.ACCEPTED)) {
                trip.get().setAvailablePlaces(trip.get().getAvailablePlaces() + 1);
                if (trip.get().getTripStatus().equals(TripStatus.BOOKED)) {
                    trip.get().setTripStatus(TripStatus.AVAILABLE);
                }
            }
            trip.get().getPassengerStatus().put(passenger.get(), PassengerStatus.REJECTED);
            return tripRepository.save(trip.get());
        }
        return null;
    }

    private Trip acceptPassenger(Long passengerID, Long tripID) {
        Optional<Trip> trip = tripRepository.findByModelIdAndIsDeleted(tripID);
        Optional<User> passenger = userRepository.findById(passengerID);
        if (trip.isPresent() && passenger.isPresent()) {
            trip.get().getPassengerStatus().put(passenger.get(), PassengerStatus.ACCEPTED);
            trip.get().setAvailablePlaces(trip.get().getAvailablePlaces() - 1);
            if (trip.get().getAvailablePlaces() == 0) {
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
            trip.get().getPassengerStatus().put(passenger.get(), PassengerStatus.ABSENT);
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
            trip.get().getPassengerStatus().values()
                    .stream()
                    .filter(k -> k.equals(PassengerStatus.PENDING))
                    .forEach(p -> p = PassengerStatus.REJECTED);
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

        if(trip.isPresent()) {
            trip.get().setTripStatus(TripStatus.CANCELED);
            return tripRepository.save(trip.get());
        }
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

        if (departureTime != null) {
            try {
                departureTimeFormat = LocalDateTime.parse(departureTime, dateTimeFormatter);
                return departureTimeFormat;
            } catch (Exception e) {
                log.error("Exception during parsing", e);
            }
        }
        return null;
    }

}

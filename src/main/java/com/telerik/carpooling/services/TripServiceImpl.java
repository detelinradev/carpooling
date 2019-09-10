package com.telerik.carpooling.services;

import com.telerik.carpooling.enums.UserRole;
import com.telerik.carpooling.enums.UserStatus;
import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.models.Car;
import com.telerik.carpooling.models.TripUserStatus;
import com.telerik.carpooling.models.Trip;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.TripDtoEdit;
import com.telerik.carpooling.models.dtos.TripDtoRequest;
import com.telerik.carpooling.models.dtos.TripDtoResponse;
import com.telerik.carpooling.models.dtos.TripUserStatusDtoResponse;
import com.telerik.carpooling.models.dtos.dtos.mapper.DtoMapper;
import com.telerik.carpooling.repositories.CarRepository;
import com.telerik.carpooling.repositories.TripUserStatusRepository;
import com.telerik.carpooling.repositories.TripRepository;
import com.telerik.carpooling.repositories.UserRepository;
import com.telerik.carpooling.services.services.contracts.TripService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Log4j2
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final TripUserStatusRepository tripUserStatusRepository;
    private final DtoMapper dtoMapper;

    @Override
    public TripDtoResponse createTrip(TripDtoRequest tripDtoRequest, String loggedUserUsername)
            throws NotFoundException {

        User driver = findUserByUsername(loggedUserUsername);
        Trip trip = dtoMapper.dtoToObject(tripDtoRequest);
        Optional<Car> car = carRepository.findByOwnerAndIsDeletedFalse(driver);

        if (!car.isPresent())
            throw new NotFoundException("Request not submitted, please create car first");

        trip.setTripStatus(TripStatus.AVAILABLE);
        trip.setIsDeleted(false);
        tripRepository.save(trip);

        TripUserStatus tripUserStatus = new TripUserStatus(driver, trip, UserStatus.DRIVER);
        tripUserStatus.setIsDeleted(false);
        tripUserStatusRepository.save(tripUserStatus);

        return dtoMapper.objectToDto(trip);
    }

    @Override
    public TripDtoResponse updateTrip(TripDtoEdit tripDtoEdit, String loggedUserUsername) {

        User user = findUserByUsername(loggedUserUsername);
        Trip trip = dtoMapper.dtoToObject(tripDtoEdit);
        List<TripUserStatus> tripUserStatusList = tripUserStatusRepository.findAllByTripAndIsDeletedFalse(trip);

        if (tripUserStatusList.stream().filter(j -> j.getUser().equals(user))
                .noneMatch(k -> k.getUserStatus().equals(UserStatus.DRIVER)))
            throw new IllegalArgumentException("The user is not the creator of the trip");

        return dtoMapper.objectToDto(tripRepository.save(trip));
    }

    @Override
    public TripDtoResponse getTrip(Long tripID) throws NotFoundException {

        Trip trip = getTripById(tripID);

        return dtoMapper.objectToDto(trip);
    }

    @Override
    public List<TripUserStatusDtoResponse> getTripUserStatus(Long tripId) throws NotFoundException {

        Trip trip = getTripById(tripId);
        List<TripUserStatus> tripUserStatus = tripUserStatusRepository.findAllByTripAndIsDeletedFalse(trip);

        return dtoMapper.tripUserStatusToDtoList(tripUserStatus);
    }

    @Override
    public List<TripDtoResponse> getTrips(Integer pageNumber, Integer pageSize, TripStatus tripStatus,
                                          String origin, String destination, String earliestDepartureTime,
                                          String latestDepartureTime, Integer availablePlaces, Boolean smoking,
                                          Boolean pets, Boolean luggage, Boolean airConditioned) {


        if (availablePlaces != null && (availablePlaces < 1 || availablePlaces > 4))
            throw new IllegalArgumentException("Available seats should be between 1 and 4");
        if ((pageNumber != null && pageSize == null) || (pageNumber == null && pageSize != null))
            throw new IllegalArgumentException("Page number and page size should be both present");

        List<Trip> trips = tripRepository.findTripsByPassedParameters(
                tripStatus, origin, destination, parseDateTime(earliestDepartureTime),
                parseDateTime(latestDepartureTime), availablePlaces,
                smoking, pets, luggage, airConditioned, (pageNumber != null ? PageRequest.of(pageNumber, pageSize) : null));

        if (trips.size() == 0) return Collections.emptyList();

        return dtoMapper.tripToDtoList(trips);
    }

    @Override
    public void deleteTrip(Long tripId, String loggedUserUsername) throws NotFoundException {

        User user = findUserByUsername(loggedUserUsername);
        Trip trip = getTripById(tripId);
        List<TripUserStatus> tripUserStatusList = tripUserStatusRepository.findAllByTripAndIsDeletedFalse(trip);

        if (tripUserStatusList.stream().filter(j -> j.getUser().equals(user))
                .noneMatch(k -> k.getUserStatus().equals(UserStatus.DRIVER)) || user.getRole().equals(UserRole.ADMIN))
            throw new IllegalArgumentException("You are not authorized to delete the trip");

        trip.setIsDeleted(true);

        tripRepository.save(trip);
    }

    @Override
    public void changeTripStatus(Long tripID, String loggedUserUsername, TripStatus tripStatus) throws NotFoundException {

        User user = findUserByUsername(loggedUserUsername);
        Trip trip = getTripById(tripID);
        List<TripUserStatus> tripUserStatusList = tripUserStatusRepository.findAllByTripAndIsDeletedFalse(trip);

        if (tripUserStatusList.stream().filter(j -> j.getUser().equals(user))
                .noneMatch(k -> k.getUserStatus().equals(UserStatus.DRIVER)))
            throw new IllegalArgumentException("You are not authorized to change trip status");

        switch (tripStatus) {
            case DONE:
                if (!trip.getTripStatus().equals(TripStatus.ONGOING))
                    throw new IllegalArgumentException("Trip is not ongoing");
                markTripAsDone(trip);
                break;
            case BOOKED:
                if (!trip.getTripStatus().equals(TripStatus.AVAILABLE))
                    throw new IllegalArgumentException("Trip is not available");
                markTripAsBooked(trip);
                break;
            case ONGOING:
                if (!trip.getTripStatus().equals(TripStatus.AVAILABLE)
                        || !trip.getTripStatus().equals(TripStatus.BOOKED))
                    throw new IllegalArgumentException("Trip is not available or booked");
                markTripAsOngoing(trip);
                break;
            case CANCELED:
                if (trip.getTripStatus().equals(TripStatus.DONE))
                    throw new IllegalArgumentException("Trip status can not be changed from done to canceled");
                markTripAsCanceled(trip);
                break;
            default:
                throw new IllegalArgumentException("Trip status not found");
        }
    }

    public void changeUserStatus(Long tripID, String passengerUsername, String loggedUserUsername,
                                 UserStatus userStatus) throws NotFoundException {

        Trip trip = getTripById(tripID);
        User driver = findUserByUsername(loggedUserUsername);
        User passenger = findUserByUsername(passengerUsername);
        List<TripUserStatus> tripUserStatusList = tripUserStatusRepository.findAllByTripAndUserAndIsDeletedFalse(trip, passenger);
        List<TripUserStatus> userStatusList = tripUserStatusRepository.findAllByTripAndIsDeletedFalse(trip);

        if (driver.equals(passenger)) {

            if (userStatus.equals(UserStatus.PENDING)) {

                if (!trip.getTripStatus().equals(TripStatus.AVAILABLE))
                    throw new IllegalArgumentException("Passenger can not be added to the trip when trip status is not available");
                if (userStatusList.stream().anyMatch(k -> k.getUser().equals(passenger)))
                    throw new IllegalArgumentException("Passenger can not be added to the trip twice");

                addPassenger(trip, passenger);

            } else if (userStatus.equals(UserStatus.CANCELED)) {

                if (userStatusList.stream().noneMatch(k -> k.getUser().equals(passenger))
                        || userStatusList.stream()
                        .filter(k -> k.getUser().equals(passenger))
                        .anyMatch(k -> k.getUserStatus().equals(UserStatus.DRIVER)))
                    throw new
                            IllegalArgumentException("You are not authorized to cancel passenger participation in the trip");

                cancelPassenger(passenger, trip);

            } else throw new IllegalArgumentException("Passenger status not found");

        } else {
            if (userStatusList.stream().filter(j -> j.getUser().equals(driver))
                    .noneMatch(k -> k.getUserStatus().equals(UserStatus.DRIVER))
                    || userStatusList.stream().noneMatch(k -> k.getUser().equals(passenger)))
                throw new IllegalArgumentException("Driver and/or passenger does not belong to this trip");
            switch (userStatus) {
                case ABSENT:
                    if (!trip.getTripStatus().equals(TripStatus.AVAILABLE)
                            || !trip.getTripStatus().equals(TripStatus.BOOKED))
                        throw new IllegalArgumentException("Trip should be AVAILABLE or BOOKED to mark passenger" +
                                " as ABSENT");
                    if (tripUserStatusList.stream()
                            .noneMatch(k -> k.getUserStatus().equals(UserStatus.ACCEPTED)))
                        throw new IllegalArgumentException("Passenger should be with passenger status ACCEPTED " +
                                "to be marked as ABSENT");

                    absentPassenger(passenger, trip);

                    break;
                case ACCEPTED:
                    if (!trip.getTripStatus().equals(TripStatus.AVAILABLE))
                        throw new IllegalArgumentException("Trip should be AVAILABLE to accept passenger");
                    if (userStatusList.stream()
                            .filter(k -> k.getUser().equals(passenger))
                            .noneMatch(k -> k.getUserStatus().equals(UserStatus.PENDING)))
                        throw new IllegalArgumentException("Passenger should be with passenger status PENDING " +
                                "to be marked as ACCEPTED");

                    acceptPassenger(passenger, trip);

                    break;
                case REJECTED:
                    rejectPassenger(passenger, trip);
                    break;
                default:
                    throw new IllegalArgumentException("Passenger status not found");

            }
        }

    }

    private void addPassenger(Trip trip, User passenger) {

        TripUserStatus tripUserStatus = new TripUserStatus(passenger, trip, UserStatus.PENDING);
        tripUserStatus.setIsDeleted(false);

        tripUserStatusRepository.save(tripUserStatus);
    }

    private void cancelPassenger(User user, Trip trip) {

        changeTripStatusAndAvailableSeatsUp(user, trip);

        TripUserStatus tripUserStatus = new TripUserStatus(user, trip, UserStatus.CANCELED);
        tripUserStatus.setIsDeleted(false);
        tripUserStatusRepository.save(tripUserStatus);
    }

    private void rejectPassenger(User passenger, Trip trip) {

        changeTripStatusAndAvailableSeatsUp(passenger, trip);
        TripUserStatus tripUserStatus = new TripUserStatus(passenger, trip, UserStatus.REJECTED);
        tripUserStatus.setIsDeleted(false);
        tripUserStatusRepository.save(tripUserStatus);

    }

    private void absentPassenger(User passenger, Trip trip) {

        changeTripStatusAndAvailableSeatsUp(passenger, trip);

        TripUserStatus tripUserStatus = new TripUserStatus(passenger, trip, UserStatus.ABSENT);
        tripUserStatus.setIsDeleted(false);
        tripUserStatusRepository.save(tripUserStatus);
    }

    private void changeTripStatusAndAvailableSeatsUp(User user, Trip trip) {
        List<TripUserStatus> tripUserStatusList = tripUserStatusRepository.findAllByTripAndIsDeletedFalse(trip);
        if (tripUserStatusList.stream()
                .filter(k -> k.getUser().equals(user))
                .anyMatch(k -> k.getUserStatus().equals(UserStatus.ACCEPTED))) {
            trip.setAvailablePlaces(trip.getAvailablePlaces() + 1);

            if (trip.getTripStatus().equals(TripStatus.BOOKED)) {
                trip.setTripStatus(TripStatus.AVAILABLE);
            }
        }
    }

    private void acceptPassenger(User passenger, Trip trip) {

        TripUserStatus tripUserStatus = new TripUserStatus(passenger, trip, UserStatus.ACCEPTED);
        tripUserStatus.setIsDeleted(false);

        trip.setAvailablePlaces(trip.getAvailablePlaces() - 1);

        if (trip.getAvailablePlaces() == 0) {
            trip.setTripStatus(TripStatus.BOOKED);
        }

        tripUserStatusRepository.save(tripUserStatus);
        tripRepository.save(trip);
    }

    private void markTripAsBooked(Trip trip) {

        List<TripUserStatus> tripUserStatusList = tripUserStatusRepository.findAllByTripAndIsDeletedFalse(trip);
        trip.setTripStatus(TripStatus.BOOKED);
        tripUserStatusList.stream()
                .filter(k -> k.getUserStatus().equals(UserStatus.PENDING))
                .forEach(j -> j.setUserStatus(UserStatus.REJECTED));
        tripUserStatusList.stream()
                .filter(k -> k.getUserStatus().equals(UserStatus.REJECTED))
                .forEach(tripUserStatusRepository::save);

        tripRepository.save(trip);
    }

    private void markTripAsOngoing(Trip trip) {

        trip.setTripStatus(TripStatus.ONGOING);
        tripRepository.save(trip);
    }

    private void markTripAsDone(Trip trip) {

        trip.setTripStatus(TripStatus.DONE);
        tripRepository.save(trip);
    }

    private void markTripAsCanceled(Trip trip) {

        trip.setTripStatus(TripStatus.CANCELED);
        tripRepository.save(trip);
    }

    private LocalDateTime parseDateTime(String departureTime) {
        LocalDateTime departureTimeFormat;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        if (departureTime != null) {
            try {
                departureTimeFormat = LocalDateTime.parse(departureTime, dateTimeFormatter);
                return departureTimeFormat;
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Date time format used is not correct");
            }
        }
        return null;
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsernameAndIsDeletedFalse(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username is not recognized"));
    }

    private Trip getTripById(Long tripID) throws NotFoundException {
        return tripRepository.findByModelIdAndIsDeletedFalse(tripID)
                .orElseThrow(() -> new NotFoundException("Trip does not exist"));
    }

}

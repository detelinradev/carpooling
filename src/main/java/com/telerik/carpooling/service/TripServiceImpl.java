package com.telerik.carpooling.service;

import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.enums.UserRole;
import com.telerik.carpooling.enums.UserStatus;
import com.telerik.carpooling.exception.MyNotFoundException;
import com.telerik.carpooling.model.Trip;
import com.telerik.carpooling.model.TripUserStatus;
import com.telerik.carpooling.model.User;
import com.telerik.carpooling.model.dto.TripDtoEdit;
import com.telerik.carpooling.model.dto.TripDtoRequest;
import com.telerik.carpooling.model.dto.TripDtoResponse;
import com.telerik.carpooling.model.dto.dto.mapper.DtoMapper;
import com.telerik.carpooling.repository.CarRepository;
import com.telerik.carpooling.repository.TripRepository;
import com.telerik.carpooling.repository.TripUserStatusRepository;
import com.telerik.carpooling.repository.UserRepository;
import com.telerik.carpooling.service.service.contract.TripService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Log4j2
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final TripUserStatusRepository tripUserStatusRepository;
    private final DtoMapper dtoMapper;

    /**
     * Creates trip from given DTO object, there is check for prerequisite that user have to create a car before
     * be able to create trip, if the criteria is met, trip is created, otherwise checked exception is thrown.
     * It is expected the input to be valid data, based on a validity check in the controller with annotation @Valid
     * and restrains upon the creation of the DTO object. However it that is not the case, validation criteria are as well
     * placed in the actual entity class as well, what would fire an exception if invalid data is provided.
     *
     * @param tripDtoRequest     DTO that holds required data for creating Trip object
     * @param loggedUserUsername username of the currently logged user extracted from the security context thread
     * @return instance of the created object as TripDtoResponse
     * @throws MyNotFoundException throws checked exception if the car has not been created upon the creating of the trip
     */
    @Override
    public TripDtoResponse createTrip(final TripDtoRequest tripDtoRequest, final String loggedUserUsername)
            throws MyNotFoundException {

        carRepository.findByOwnerAndIsDeletedFalse(loggedUserUsername)
                .orElseThrow(() -> new MyNotFoundException("Request not submitted, please create car first"));

        return dtoMapper.objectToDto(tripRepository.save(dtoMapper.dtoToObject(tripDtoRequest)));
    }

    /**
     * Updates trip from given DTO object, there is check if the logged user is the owner of the trip, if the
     * criteria is met, the trip is updated, otherwise IllegalArgumentException exception is thrown as that is not the
     * expected behavior.
     * It is expected the input to be valid data, based on a validity check in the controller with annotation @Valid
     * and restrains upon the creation of the DTO object. However it that is not the case, validation criteria are as well
     * placed in the actual entity class as well, what would fire an exception if invalid data is provided.
     *
     * @param tripDtoEdit DTO that holds required data for updating Trip object
     * @param loggedUserUsername username of the currently logged user extracted from the security context thread
     * @return instance of the updated object as TripDtoResponse
     */
    @Override
    public TripDtoResponse updateTrip(final TripDtoEdit tripDtoEdit, final String loggedUserUsername) {

        tripUserStatusRepository.findOneByTripModelIdAndUserUsernameAsDriver(tripDtoEdit.getModelId(), loggedUserUsername)
                .orElseThrow(() -> new IllegalArgumentException("The user is not the owner of the trip"));

        return dtoMapper.objectToDto(tripRepository.save(dtoMapper.dtoToObject(tripDtoEdit)));
    }

    @Override
    public void deleteTrip(Long tripId, String loggedUserUsername) throws MyNotFoundException {

        User user = findUserByUsername(loggedUserUsername);
        Trip trip = getTripById(tripId);
        List<TripUserStatus> tripUserStatusList = tripUserStatusRepository.findAllTripsWithDriversByTripAndIsDeletedFalse(trip);

        if (tripUserStatusList.stream().filter(j -> j.getUser().equals(user))
                .noneMatch(k -> (k.getUserStatus().equals(UserStatus.DRIVER)) || user.getRole().equals(UserRole.ADMIN)))
            throw new IllegalArgumentException("You are not authorized to delete the trip");

        trip.setIsDeleted(true);

        tripRepository.save(trip);
    }

    @Override
    public void changeTripStatus(Long tripID, String loggedUserUsername, TripStatus tripStatus) throws MyNotFoundException {

        User user = findUserByUsername(loggedUserUsername);
        Trip trip = getTripById(tripID);
        List<TripUserStatus> tripUserStatusList = tripUserStatusRepository.findAllTripsWithDriversByTripAndIsDeletedFalse(trip);

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
                                 UserStatus userStatus) throws MyNotFoundException {

        Trip trip = getTripById(tripID);
        User driver = findUserByUsername(loggedUserUsername);
        User passenger = findUserByUsername(passengerUsername);
        List<TripUserStatus> tripUserStatusList = tripUserStatusRepository.findAllByTripAndUserAndIsDeletedFalse(trip, passenger);
        List<TripUserStatus> userStatusList = tripUserStatusRepository.findAllTripsWithDriversByTripAndIsDeletedFalse(trip);

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
        List<TripUserStatus> tripUserStatusList = tripUserStatusRepository.findAllTripsWithDriversByTripAndIsDeletedFalse(trip);
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

        List<TripUserStatus> tripUserStatusList = tripUserStatusRepository.findAllTripsWithDriversByTripAndIsDeletedFalse(trip);
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

    private User findUserByUsername(String username) {
        return userRepository.findByUsernameAndIsDeletedFalse(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username is not recognized"));
    }

    private Trip getTripById(Long tripID) throws MyNotFoundException {
        return tripRepository.findByModelIdAndIsDeletedFalse(tripID)
                .orElseThrow(() -> new MyNotFoundException("Trip does not exist"));
    }

}
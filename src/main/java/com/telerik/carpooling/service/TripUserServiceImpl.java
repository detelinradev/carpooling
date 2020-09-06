package com.telerik.carpooling.service;

import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.enums.UserStatus;
import com.telerik.carpooling.exception.MyNotFoundException;
import com.telerik.carpooling.model.Trip;
import com.telerik.carpooling.model.TripUserStatus;
import com.telerik.carpooling.model.User;
import com.telerik.carpooling.model.dto.TripUserStatusDtoResponse;
import com.telerik.carpooling.model.dto.dto.mapper.DtoMapper;
import com.telerik.carpooling.repository.TripRepository;
import com.telerik.carpooling.repository.TripUserStatusRepository;
import com.telerik.carpooling.repository.UserRepository;
import com.telerik.carpooling.service.service.contract.TripUserStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
@Log4j2
public class TripUserServiceImpl implements TripUserStatusService {

    private final TripUserStatusRepository tripUserStatusRepository;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final DtoMapper dtoMapper;

    @Override
    public TripUserStatusDtoResponse createTripUserStatusAsDriver(Trip trip, String loggedUserUsername) {

        User driver = findUserByUsername(loggedUserUsername);

        TripUserStatus tripUserStatus = new TripUserStatus(driver, trip, UserStatus.DRIVER);
        tripUserStatusRepository.save(tripUserStatus);

        return dtoMapper.objectToDtoTrip(tripUserStatus);
    }

    @Override
    public List<TripUserStatusDtoResponse> getTripUserStatus(Long tripId) throws MyNotFoundException {

        Trip trip = getTripById(tripId);
        List<TripUserStatus> tripUserStatus = tripUserStatusRepository.findAllTripsWithDriversByTripAndIsDeletedFalse(trip);

        return dtoMapper.tripUserStatusToDtoList(tripUserStatus);
    }

    @Override
    public List<TripUserStatusDtoResponse> userOwnTripsWithDrivers(String username) {
        User user = findUserByUsername(username);
        return dtoMapper.tripUserStatusToDtoList(tripUserStatusRepository.findAllUserTripsWithItsDrivers(UserStatus.DRIVER, user));
    }

    @Override
    public List<TripUserStatusDtoResponse> getTripUserStatuses(Long tripID) throws MyNotFoundException {

        Trip trip = getTripById(tripID);
        List<TripUserStatus> result = new ArrayList<>(tripUserStatusRepository.findAllTripsWithDriversByTripAndIsDeletedFalse(trip).stream()
                .collect(Collectors
                        .toMap(TripUserStatus::getUser, Function.identity(), (u1, u2) -> u1))
                .values());

        return dtoMapper.tripUserStatusToDtoList(result);
    }

    @Override
    public List<TripUserStatusDtoResponse> getAllTripUserStatuses(Integer pageNumber, Integer pageSize, TripStatus tripStatus,
                                                                  String origin, String destination, String earliestDepartureTime,
                                                                  String latestDepartureTime, Integer availablePlaces, Boolean smoking,
                                                                  Boolean pets, Boolean luggage, Boolean airConditioned) {


        if (availablePlaces != null && (availablePlaces < 1 || availablePlaces > 4))
            throw new IllegalArgumentException("Available seats should be between 1 and 4");
        if ((pageNumber != null && pageSize == null) || (pageNumber == null && pageSize != null))
            throw new IllegalArgumentException("Page number and page size should be both present");

        List<TripUserStatus> tripUserStatuses = tripUserStatusRepository.findTripUserStatusesByPassedParameters(
                tripStatus, origin, destination, parseDateTime(earliestDepartureTime),
                parseDateTime(latestDepartureTime), availablePlaces,
                smoking, pets, luggage, airConditioned, (pageNumber != null ? PageRequest.of(pageNumber, pageSize) : null));

        if (tripUserStatuses.size() == 0) return Collections.emptyList();

        return dtoMapper.tripUserStatusToDtoList(tripUserStatuses);
    }

    /**
     * Changes <class>UserStatus</class> of this passenger which <class>username</class> is passed as parameter.
     * <p>
     * In case the logged <class>user</class> does not belong to <class>trip</class> exception is thrown with relevant
     * message, except for PENDING <class>userStatus</class>. In case logged <class>user</class> is trying to change
     * passenger status and is not a driver exception is thrown with relevant message.
     * <p>
     * Various stages should be logically connected and if case there is mismatch exception is thrown with matching
     * message.
     * Accepted paths are PENDING-ACCEPTED-ABSENT or PENDING-ACCEPTED, CANCELED is accepted from a passenger, REJECTED
     * is accepted from a driver, ABSENT is accepted from a driver in case previous <class>userStatus</class> of
     * the passenger is ACCEPTED.
     * <p>
     * Transactional annotation is added to override class based behavior read only = true, with read only = false, as
     * this method is modifying the entity so we expect Hibernate to observe changes in the current Persistence Context
     * and include update at flush-time.
     *
     * @param tripID  the <class>modelId</class> of the <class>trip</class>
     * @param passengerUsername the <class>username</class> of this<class>user</class> which <class>userStatus</class>
     *                          is passed for change
     * @param loggedUserUsername <class>username</class> of the currently logged <class>user</class> extracted from
     *      *                           the security context thread
     * @param userStatus  the new <class>UserStatus</class> to be applied for this passenger
     */
    @Override
    @Transactional
    public void changeUserStatus(Long tripID, String passengerUsername, String loggedUserUsername,
                                 UserStatus userStatus) {

        Trip trip = getTripById(tripID);
        User loggedUser = findUserByUsername(loggedUserUsername);
        User passenger = findUserByUsername(passengerUsername);
        List<TripUserStatus> tripUserStatusList = tripUserStatusRepository.findAllByTripModelIdAndIsDeletedFalse(tripID);

        switch (userStatus) {

            case PENDING:
                addPassenger(passenger, loggedUser, trip, tripUserStatusList);
                break;

            case CANCELED:
                cancelPassenger(passenger, loggedUser, trip, tripUserStatusList);
                break;

            case ABSENT:
                absentPassenger(passenger, loggedUser, trip, tripUserStatusList);
                break;

            case ACCEPTED:
                acceptPassenger(passenger, loggedUser, trip, tripUserStatusList);
                break;

            case REJECTED:
                rejectPassenger(passenger, loggedUser, trip, tripUserStatusList);
                break;

            default:
                throw new IllegalArgumentException("Passenger status not found");

        }
    }

    private void addPassenger(User passenger, User loggedUser, Trip trip, List<TripUserStatus> userStatusList) {

        if (userStatusList.stream().noneMatch(k -> k.getUser().equals(loggedUser))) {

            if (!trip.getTripStatus().equals(TripStatus.AVAILABLE))
                throw new IllegalArgumentException("Passenger can not be added to the trip when trip status is not available");

            TripUserStatus tripUserStatus = new TripUserStatus(passenger, trip, UserStatus.PENDING);
            tripUserStatusRepository.save(tripUserStatus);

        } else throw new IllegalArgumentException("Passenger can not join trip twice");
    }

    private void cancelPassenger(User passenger, User loggedUser, Trip trip, List<TripUserStatus> userStatusList) {

        if (userStatusList
                .stream()
                .anyMatch(m -> m.getUser().equals(passenger))) {
            if (userStatusList
                    .stream()
                    .filter(m -> m.getUserStatus().equals(UserStatus.DRIVER))
                    .noneMatch(k -> k.getUser().equals(loggedUser))) {

                changeTripStatusAndAvailableSeatsUp(passenger, trip, userStatusList);

                TripUserStatus tripUserStatus = new TripUserStatus(passenger, trip, UserStatus.CANCELED);
                tripUserStatusRepository.save(tripUserStatus);

            } else throw new IllegalArgumentException("Driver can not CANCEL passenger, use REJECT passenger instead");
        } else throw new IllegalArgumentException("Logged user does not belong to the trip");
    }

    private void rejectPassenger(User passenger, User loggedUser, Trip trip, List<TripUserStatus> userStatusList) {

        if (userStatusList.stream()
                .filter(m -> m.getUserStatus().equals(UserStatus.DRIVER))
                .anyMatch(k -> k.getUser().equals(loggedUser))) {

            changeTripStatusAndAvailableSeatsUp(passenger, trip, userStatusList);

            TripUserStatus tripUserStatus = new TripUserStatus(passenger, trip, UserStatus.REJECTED);
            tripUserStatusRepository.save(tripUserStatus);

        } else throw new IllegalArgumentException("Only driver can REJECT passenger");
    }

    private void absentPassenger(User passenger, User loggedUser, Trip trip, List<TripUserStatus> userStatusList) {

        if (userStatusList.stream()
                .filter(m -> m.getUserStatus().equals(UserStatus.DRIVER))
                .anyMatch(k -> k.getUser().equals(loggedUser))) {

            if (trip.getTripStatus().equals(TripStatus.AVAILABLE)
                    || trip.getTripStatus().equals(TripStatus.BOOKED)) {

                if (userStatusList.stream()
                        .filter(m -> m.getUser().equals(passenger))
                        .noneMatch(k -> k.getUserStatus().equals(UserStatus.ACCEPTED)))
                    throw new IllegalArgumentException("Passenger should be with passenger status ACCEPTED " +
                            "to be marked as ABSENT");

                changeTripStatusAndAvailableSeatsUp(passenger, trip, userStatusList);

                TripUserStatus tripUserStatus = new TripUserStatus(passenger, trip, UserStatus.ABSENT);
                tripUserStatusRepository.save(tripUserStatus);

            } else throw new IllegalArgumentException("Trip should be AVAILABLE or BOOKED to mark passenger as ABSENT");

        } else throw new IllegalArgumentException("Only driver can mark passenger as ABSENT");
    }

    private void changeTripStatusAndAvailableSeatsUp(User user, Trip trip, List<TripUserStatus> userStatusList) {

        if (userStatusList.stream()
                .filter(k -> k.getUser().equals(user))
                .anyMatch(k -> k.getUserStatus().equals(UserStatus.ACCEPTED))) {
            trip.setAvailablePlaces(trip.getAvailablePlaces() + 1);

            if (trip.getTripStatus().equals(TripStatus.BOOKED)) {
                trip.setTripStatus(TripStatus.AVAILABLE);
            }
        }
    }

    private void acceptPassenger(User passenger, User loggedUser, Trip trip, List<TripUserStatus> userStatusList) {

        if (userStatusList.stream()
                .filter(m -> m.getUserStatus().equals(UserStatus.DRIVER))
                .anyMatch(k -> k.getUser().equals(loggedUser))) {

            if (!trip.getTripStatus().equals(TripStatus.AVAILABLE))
                throw new IllegalArgumentException("Trip should be AVAILABLE to accept passenger");

            if (userStatusList.stream()
                    .filter(k -> k.getUser().equals(passenger))
                    .noneMatch(k -> k.getUserStatus().equals(UserStatus.PENDING)))
                throw new IllegalArgumentException("Passenger should be with passenger status PENDING " +
                        "to be marked as ACCEPTED");

            TripUserStatus tripUserStatus = new TripUserStatus(passenger, trip, UserStatus.ACCEPTED);

            trip.setAvailablePlaces(trip.getAvailablePlaces() - 1);

            if (trip.getAvailablePlaces() == 0) {
                trip.setTripStatus(TripStatus.BOOKED);
            }

            tripUserStatusRepository.save(tripUserStatus);
            tripRepository.save(trip);

        } else throw new IllegalArgumentException("Only driver can mark passenger as ACCEPTED");
    }


    private User findUserByUsername(String username) {
        return userRepository.findByUsernameAndIsDeletedFalse(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username is not recognized"));
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

    private Trip getTripById(Long tripID) throws MyNotFoundException {
        return tripRepository.findByModelIdAndIsDeletedFalse(tripID)
                .orElseThrow(() -> new MyNotFoundException("Trip does not exist"));
    }
}

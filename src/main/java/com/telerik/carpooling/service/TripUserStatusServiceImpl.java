package com.telerik.carpooling.service;

import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.enums.UserStatus;
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

import javax.validation.constraints.NotNull;
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
public class TripUserStatusServiceImpl implements TripUserStatusService {

    private final TripUserStatusRepository tripUserStatusRepository;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final DtoMapper dtoMapper;

    @Transactional
    @Override
    public TripUserStatusDtoResponse createTripUserStatusAsDriver(final Trip trip, final String loggedUserUsername) {

        User driver = findUserByUsername(loggedUserUsername);

        TripUserStatus tripUserStatus = new TripUserStatus(driver, trip, UserStatus.DRIVER);

        tripUserStatusRepository.save(tripUserStatus);

        return dtoMapper.objectToDtoTrip(tripUserStatus);
    }

    @Override
    public List<TripUserStatusDtoResponse> getUserOwnTripsWithDrivers(final String loggedUserUsername) {

        return dtoMapper.tripUserStatusToDtoList(tripUserStatusRepository.findAllUserOwnTripsWithDrivers(loggedUserUsername));
    }

    @Override
    public List<TripUserStatusDtoResponse> getTripUserStatuses(final Long tripID) {

        Trip trip = getTripById(tripID);
        List<TripUserStatus> result = new ArrayList<>(tripUserStatusRepository.findAllTripsWithDriversByTripAndIsDeletedFalse(trip).stream()
                .collect(Collectors
                        .toMap(TripUserStatus::getUser, Function.identity(), (u1, u2) -> u1))
                .values());

        return dtoMapper.tripUserStatusToDtoList(result);
    }

    @Override
    public List<TripUserStatusDtoResponse> getAllTripUserStatuses(final Integer pageNumber,final Integer pageSize,
                                                                  final TripStatus tripStatus,final String origin,
                                                                  final String destination,
                                                                  final String earliestDepartureTime,
                                                                  final String latestDepartureTime,
                                                                  final Integer availablePlaces,final Boolean smoking,
                                                                  final  Boolean pets,final Boolean luggage,
                                                                  final Boolean airConditioned) {


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

    @Override
    @Transactional
    public void changeUserStatus(final Long tripID,final String passengerUsername,final String loggedUserUsername,
                                final UserStatus userStatus) {

        Trip trip = getTripById(tripID);
        User loggedUser = findUserByUsername(loggedUserUsername);
        User passenger = findUserByUsername(passengerUsername);
        List<TripUserStatus> tripUserStatusList = tripUserStatusRepository.findAllByTripModelIdAndIsDeletedFalse(tripID);

        TripUserStatus tripUserStatus = userStatus.changeUserStatus(passenger,loggedUser,trip,
                tripUserStatusList,this);

        tripUserStatusRepository.save(tripUserStatus);

    }

    @Override
    public void adjustAvailablePlacesAndTripStatusWhenPassengerIsAccepted(@NotNull final Trip trip){

        trip.setAvailablePlaces(trip.getAvailablePlaces() - 1);

        if (trip.getAvailablePlaces() == 0) {

                    trip.setTripStatus(TripStatus.BOOKED);
                }
        tripRepository.save(trip);
    }


    private User findUserByUsername(final String username) {
        return userRepository.findByUsernameAndIsDeletedFalse(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username is not recognized"));
    }

    private LocalDateTime parseDateTime(final String departureTime) {
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

    private Trip getTripById(final Long tripID) {
        return tripRepository.findByModelIdAndIsDeletedFalse(tripID)
                .orElseThrow(() -> new IllegalArgumentException("Trip does not exist"));
    }
}

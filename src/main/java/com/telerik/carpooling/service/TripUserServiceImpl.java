package com.telerik.carpooling.service;

import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.enums.UserStatus;
import com.telerik.carpooling.exception.MyNotFoundException;
import com.telerik.carpooling.model.Trip;
import com.telerik.carpooling.model.TripUserStatus;
import com.telerik.carpooling.model.User;
import com.telerik.carpooling.model.dto.TripDtoRequest;
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
@Log4j2
public class TripUserServiceImpl implements TripUserStatusService {

    private final TripUserStatusRepository tripUserStatusRepository;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final DtoMapper dtoMapper;

    @Override
    public TripUserStatusDtoResponse createTripUserStatus(TripDtoRequest tripDtoRequest, String loggedUserUsername) {

        User driver = findUserByUsername(loggedUserUsername);
        Trip trip = dtoMapper.dtoToObject(tripDtoRequest);

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

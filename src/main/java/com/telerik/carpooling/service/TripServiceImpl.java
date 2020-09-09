package com.telerik.carpooling.service;

import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.enums.UserStatus;
import com.telerik.carpooling.exception.MyNotFoundException;
import com.telerik.carpooling.model.Trip;
import com.telerik.carpooling.model.TripUserStatus;
import com.telerik.carpooling.model.base.MappedAudibleBase;
import com.telerik.carpooling.model.dto.TripDtoEdit;
import com.telerik.carpooling.model.dto.TripDtoRequest;
import com.telerik.carpooling.model.dto.TripDtoResponse;
import com.telerik.carpooling.model.dto.dto.mapper.DtoMapper;
import com.telerik.carpooling.repository.CarRepository;
import com.telerik.carpooling.repository.TripRepository;
import com.telerik.carpooling.repository.TripUserStatusRepository;
import com.telerik.carpooling.service.service.contract.TripService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
@Log4j2
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;
    private final CarRepository carRepository;
    private final TripUserStatusRepository tripUserStatusRepository;
    private final DtoMapper dtoMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Trip createTrip(final TripDtoRequest tripDtoRequest, final String loggedUserUsername)
            throws MyNotFoundException {

        carRepository.findByOwnerAndIsDeletedFalse(loggedUserUsername)
                .orElseThrow(() -> new MyNotFoundException("Request not submitted, please create car first"));

        return tripRepository.save(dtoMapper.dtoToObject(tripDtoRequest));
    }

    @Override
    public TripDtoResponse updateTrip(final TripDtoEdit tripDtoEdit, final String loggedUserUsername) {

        tripUserStatusRepository.findFirstByTripModelIdAndUserUsernameAsDriver(tripDtoEdit.getModelId(), loggedUserUsername)
                .orElseThrow(() -> new IllegalArgumentException("The user is not the owner of the trip"));

        return dtoMapper.objectToDto(tripRepository.save(dtoMapper.dtoToObject(tripDtoEdit)));
    }

    @Override
    public void deleteTrip(final Long tripId, final String loggedUserUsername) {

        tripUserStatusRepository.findOneByTripModelIdAndUserAsDriverOrAdmin(tripId, loggedUserUsername)
                .orElseThrow(() -> new IllegalArgumentException("You are not authorized to delete the trip"));

        Trip trip = getTripById(tripId);

        List<TripUserStatus> tus = tripUserStatusRepository.findAllByTripModelIdAndIsDeletedFalse(tripId);

        tus.forEach(tripUserStatus -> tripUserStatus.setIsDeleted(true));
        tus.forEach(tripUserStatusRepository::save);

        trip.setIsDeleted(true);

        tripRepository.save(trip);
    }

    @Override
    public void changeTripStatus(final Long tripID,final String loggedUserUsername,final TripStatus newTripStatus) {

        tripUserStatusRepository.findFirstByTripModelIdAndUserUsernameAsDriver(tripID, loggedUserUsername)
                .orElseThrow(() -> new IllegalArgumentException("Current user is not authorized to change trip status"));

        Trip trip = getTripById(tripID);

        trip = newTripStatus.changeTripStatus(trip, this);

        tripRepository.save(trip);
    }

    @Override
    public void changeAllLeftPendingUserStatusesToRejected(final Trip trip) {

        // fetch all tripUserStatuses for the given trip
        List<TripUserStatus> tripUserStatusList = tripUserStatusRepository
                .findAllByTripModelIdAndIsDeletedFalse(trip.getModelId());

        // use the generated list to map all users as keys, and to each user as value this tripUserStatus that is with
        // greatest modelId, means most recent one, and thus the one that is his current status
        // then filters these with PENDING status and creates new TripUserStatus with status REJECTED, what effectively
        // changes the user's current status
        tripUserStatusList.stream()
                .collect(Collectors.toMap(TripUserStatus::getUser, Function.identity(), BinaryOperator.maxBy(
                        Comparator.comparingLong(MappedAudibleBase::getModelId))))
                .values().stream()
                .filter(k -> k.getUserStatus().equals(UserStatus.PENDING))
                .map(n -> new TripUserStatus(n.getUser(), trip, UserStatus.REJECTED))
                .forEach(tripUserStatusRepository::save);
    }

    private Trip getTripById(Long tripId) {
        return tripRepository.findByModelIdAndIsDeletedFalse(tripId)
                .orElseThrow(() -> new IllegalArgumentException("Trip with this modelId does not exist"));
    }
}

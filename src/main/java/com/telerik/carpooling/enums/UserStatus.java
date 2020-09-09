package com.telerik.carpooling.enums;

import com.telerik.carpooling.model.Trip;
import com.telerik.carpooling.model.TripUserStatus;
import com.telerik.carpooling.model.User;
import com.telerik.carpooling.service.service.contract.TripUserStatusService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum UserStatus {

    PENDING("A"){

        @Override
        public TripUserStatus changeUserStatus(User passenger, User loggedUser, Trip trip,
                                               List<TripUserStatus> tripUserStatusList,
                                               TripUserStatusService tripUserStatusService) {
            if (tripUserStatusList.stream().noneMatch(k -> k.getUser().equals(loggedUser))) {

                if (!trip.getTripStatus().equals(TripStatus.AVAILABLE))
                    throw new IllegalArgumentException("Passenger can not be added to the trip when trip status is not available");

                return new TripUserStatus(passenger, trip, UserStatus.PENDING);

            } else throw new IllegalArgumentException("Passenger can not join trip twice");

        }
    }, ACCEPTED("B") {

        @Override
        public TripUserStatus changeUserStatus(User passenger, User loggedUser, Trip trip,
                                               List<TripUserStatus> tripUserStatusList,
                                               TripUserStatusService tripUserStatusService) {

            if (tripUserStatusList.stream()
                    .filter(m -> m.getUserStatus().equals(UserStatus.DRIVER))
                    .anyMatch(k -> k.getUser().equals(loggedUser))) {

                if (!trip.getTripStatus().equals(TripStatus.AVAILABLE))
                    throw new IllegalArgumentException("Trip should be AVAILABLE to accept passenger");

                if (tripUserStatusList.stream()
                        .filter(k -> k.getUser().equals(passenger))
                        .noneMatch(k -> k.getUserStatus().equals(UserStatus.PENDING)))
                    throw new IllegalArgumentException("Passenger should be with passenger status PENDING " +
                            "to be marked as ACCEPTED");

                tripUserStatusService.adjustAvailablePlacesAndTripStatusWhenPassengerIsAccepted(trip);

                return new TripUserStatus(passenger, trip, UserStatus.ACCEPTED);

            } else throw new IllegalArgumentException("Only driver can mark passenger as ACCEPTED");
        }
    }, REJECTED("C") {

        @Override
        public TripUserStatus changeUserStatus(User passenger, User loggedUser, Trip trip,
                                               List<TripUserStatus> tripUserStatusList,
                                               TripUserStatusService tripUserStatusService) {
            if (tripUserStatusList.stream()
                    .filter(m -> m.getUserStatus().equals(UserStatus.DRIVER))
                    .anyMatch(k -> k.getUser().equals(loggedUser))) {

                changeTripStatusAndAvailableSeatsUp(passenger, trip, tripUserStatusList);

                return new TripUserStatus(passenger, trip, UserStatus.REJECTED);

            } else throw new IllegalArgumentException("Only driver can REJECT passenger");
        }
    },
    CANCELED("D") {

        @Override
        public TripUserStatus changeUserStatus(User passenger, User loggedUser, Trip trip,
                                               List<TripUserStatus> tripUserStatusList,
                                               TripUserStatusService tripUserStatusService) {

            if (tripUserStatusList
                    .stream()
                    .anyMatch(m -> m.getUser().equals(passenger))) {

                if (tripUserStatusList
                        .stream()
                        .filter(m -> m.getUserStatus().equals(UserStatus.DRIVER))
                        .noneMatch(k -> k.getUser().equals(loggedUser))) {

                    changeTripStatusAndAvailableSeatsUp(passenger, trip, tripUserStatusList);

                    return new TripUserStatus(passenger, trip, UserStatus.CANCELED);

                } else throw new IllegalArgumentException("Driver can not CANCEL passenger, use REJECT passenger instead");

            } else throw new IllegalArgumentException("Logged user does not belong to the trip");
        }
    }, ABSENT("E") {

        @Override
        public TripUserStatus changeUserStatus(User passenger, User loggedUser, Trip trip,
                                               List<TripUserStatus> tripUserStatusList,
                                               TripUserStatusService tripUserStatusService) {

            if (tripUserStatusList.stream()
                    .filter(m -> m.getUserStatus().equals(UserStatus.DRIVER))
                    .anyMatch(k -> k.getUser().equals(loggedUser))) {

                if (trip.getTripStatus().equals(TripStatus.AVAILABLE)
                        || trip.getTripStatus().equals(TripStatus.BOOKED)) {

                    if (tripUserStatusList.stream()
                            .filter(m -> m.getUser().equals(passenger))
                            .noneMatch(k -> k.getUserStatus().equals(UserStatus.ACCEPTED)))
                        throw new IllegalArgumentException("Passenger should be with passenger status ACCEPTED " +
                                "to be marked as ABSENT");

                    changeTripStatusAndAvailableSeatsUp(passenger, trip, tripUserStatusList);

                    return new TripUserStatus(passenger, trip, UserStatus.ABSENT);

                } else throw new IllegalArgumentException("Trip should be AVAILABLE or BOOKED to mark passenger as ABSENT");

            } else throw new IllegalArgumentException("Only driver can mark passenger as ABSENT");
        }
    }, DRIVER("F") {

        @Override
        public TripUserStatus changeUserStatus(User passenger, User loggedUser, Trip trip,
                                               List<TripUserStatus> tripUserStatusList,
                                               TripUserStatusService tripUserStatusService) {
            return null;
        }
    };

    private final String code;

    abstract public TripUserStatus changeUserStatus(User passenger, User loggedUser, Trip trip,
                                                    List<TripUserStatus> tripUserStatusList,
                                                    TripUserStatusService tripUserStatusService);

    private static void changeTripStatusAndAvailableSeatsUp(User user, Trip trip, List<TripUserStatus> userStatusList) {

        if (userStatusList.stream()
                .filter(k -> k.getUser().equals(user))
                .anyMatch(k -> k.getUserStatus().equals(UserStatus.ACCEPTED))) {
            trip.setAvailablePlaces(trip.getAvailablePlaces() + 1);

            if (trip.getTripStatus().equals(TripStatus.BOOKED)) {
                trip.setTripStatus(TripStatus.AVAILABLE);
            }
        }
    }
}

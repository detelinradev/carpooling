package com.telerik.carpooling.enums;

import com.telerik.carpooling.model.Trip;
import com.telerik.carpooling.service.service.contract.TripService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum TripStatus {
    AVAILABLE("A"){
        @Override
        public Trip changeTripStatus(Trip trip, TripService tripService) {

            return trip;
        }
    },
    BOOKED("B"){
        @Override
        public Trip changeTripStatus(Trip trip, TripService tripService) {

            trip.setTripStatus(TripStatus.BOOKED);

            tripService.changeAllLeftPendingUserStatusesToRejected(trip);

            return trip;
        }
    },
    ONGOING("C") {
        @Override
        public Trip changeTripStatus(Trip trip, TripService tripService) {

            if (trip.getTripStatus().equals(TripStatus.AVAILABLE)) {

                trip.setTripStatus(TripStatus.ONGOING);

            } else throw new IllegalArgumentException("Trip should be AVAILABLE or BOOKED before be marked as ONGOING");

            return trip;
        }
    },
    DONE("D") {
        @Override
        public Trip changeTripStatus(Trip trip, TripService tripService) {

            if (trip.getTripStatus().equals(TripStatus.ONGOING)){

                trip.setTripStatus(TripStatus.DONE);

            } else  throw new IllegalArgumentException("Trip should be ONGOING before be marked as DONE");

            return trip;
        }
    },
    CANCELED("E") {
        @Override
        public Trip changeTripStatus(Trip trip, TripService tripService) {

            if (trip.getTripStatus().equals(TripStatus.DONE))
                throw new IllegalArgumentException("Trip status can not be changed once is done");

            trip.setTripStatus(TripStatus.CANCELED);

            return trip;
        }
    };

    private final String code;

    public abstract Trip changeTripStatus(Trip trip, TripService tripService);
}

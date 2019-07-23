package com.telerik.carpooling.models;

import com.telerik.carpooling.enums.PassengerStatus;
import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.models.base.MappedAudibleBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Status extends MappedAudibleBase {

    private Trip trip;

    private User passenger;

    private TripStatus oldTripStatus;

    private TripStatus newTripStatus;

    private PassengerStatus oldPassengerStatus;

    private PassengerStatus newPassengerStatus;

    public Status(Trip trip, TripStatus oldTripStatus, TripStatus newTripStatus) {
        this.trip = trip;
        this.oldTripStatus = oldTripStatus;
        this.newTripStatus = newTripStatus;
    }

    public Status(User passenger, PassengerStatus oldPassengerStatus, PassengerStatus newPassengerStatus) {
        this.passenger = passenger;
        this.oldPassengerStatus = oldPassengerStatus;
        this.newPassengerStatus = newPassengerStatus;
    }
}


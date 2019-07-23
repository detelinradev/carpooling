package com.telerik.carpooling.models;

import com.telerik.carpooling.enums.PassengerStatus;
import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.models.base.MappedAudibleBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class HistoryStatus extends MappedAudibleBase {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip",nullable = false)
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "passenger")
    private User passenger;

    private TripStatus oldTripStatus;

    private TripStatus newTripStatus;

    private PassengerStatus oldPassengerStatus;

    private PassengerStatus newPassengerStatus;

    public HistoryStatus(Trip trip, TripStatus oldTripStatus, TripStatus newTripStatus) {
        this.trip = trip;
        this.oldTripStatus = oldTripStatus;
        this.newTripStatus = newTripStatus;
    }
}


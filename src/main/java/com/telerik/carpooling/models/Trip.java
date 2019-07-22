package com.telerik.carpooling.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telerik.carpooling.enums.PassengerStatus;
import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.models.base.MappedAudibleBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Trip extends MappedAudibleBase {

    private String startingPoint;

    private String endPoint;

    private String departureDate;

    private String departureTime;

    private int tripDuration;

    private int costPerPassenger;

    private String message;

    private TripStatus tripStatus;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver", nullable = false)
    private User driver;

    @ElementCollection
    @MapKeyColumn(name = "id")
    private Map<User, PassengerStatus> passengerStatus = new HashMap<>();

    @JsonIgnore
    @ManyToMany
    private Set<User> passengersAvailableForRate = new HashSet<>();

    @JsonIgnore
    @ManyToMany
    private Set<User> passengersAllowedToRate = new HashSet<>();

    @JsonIgnore
    @ManyToMany
    private Set<User> passengersAvailableForFeedback = new HashSet<>();

    @JsonIgnore
    @ManyToMany
    private Set<User> passengersAllowedToGiveFeedback = new HashSet<>();


}

package com.telerik.carpooling.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telerik.carpooling.enums.PassengerStatus;
import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.models.base.MappedAudibleBase;
import com.telerik.carpooling.models.dtos.UserDtoResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import java.util.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Audited
public class Trip extends MappedAudibleBase {

    private String origin;

    private String destination;

    private String departureTime;

    @Column(nullable = false)
    @Range(min = 1,max = 8, message = "Please enter total number of seats between 1 and 8!")
    private int availablePlaces;

    private int tripDuration;

    private int costPerPassenger;

    private String message;

    private TripStatus tripStatus;

    @OneToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Comment> comments = new HashSet<>();


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver", nullable = false)
    private User driver;

    @JsonIgnore
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

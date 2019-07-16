package com.telerik.carpooling.models;

import com.telerik.carpooling.models.base.MappedAudibleBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Trip extends MappedAudibleBase {

    private String startingPoint;

    private String endPoint;

    private LocalDateTime departureTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car", nullable = false)
    private Car car;

//    private String carModel;
//
//    private boolean isAirConditioned;
//
//    private boolean isSmokingAllowed;
//
//    private boolean isLuggageAllowed;
//
//    private boolean isPetsFriendly;
//
//    private boolean isReturnJourney;
//
//    private boolean AreChildrenUnder7Allowed;
//
//    @Range(min = 1,max = 7)
//    private int freeSeats;

    @Size(max = 200)
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver",nullable = false)
    private User driver;

    @ManyToMany
    private Set<User> acceptedPassengers = new HashSet<>();

    @ManyToMany
    private Set<User> rejectedPassengers = new HashSet<>();

    @ManyToMany
    private Set<User> pendingPassengers = new HashSet<>();

}

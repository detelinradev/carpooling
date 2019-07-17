package com.telerik.carpooling.models.dtos;

import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.base.MappedAudibleBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TripDto {

    private String startingPoint;

    private String endPoint;

    private String departureDate;

    private String departureTime;

    private int tripDuration;

    private int costPerPassenger;

    private String carModel;

    private boolean isAirConditioned;

    private boolean isSmokingAllowed;

    private boolean isLuggageAllowed;

    private boolean isPetsFriendly;

    @Range(min = 1,max = 8)
    private int freeSeats;

    @Size(max = 200)
    private String message;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "driver",nullable = false)
//    private User driver;
//
//    @ManyToMany
//    private Set<User> acceptedPassengers = new HashSet<>();
//
//    @ManyToMany
//    private Set<User> rejectedPassengers = new HashSet<>();
//
//    @ManyToMany
//    private Set<User> pendingPassengers = new HashSet<>();

}

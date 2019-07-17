package com.telerik.carpooling.models;

import com.telerik.carpooling.models.base.MappedAudibleBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    private String departureDate;

    private String departureTime;

    private int tripDuration;

    private int costPerPassenger;

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

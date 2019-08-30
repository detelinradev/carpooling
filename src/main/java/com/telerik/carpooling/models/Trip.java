package com.telerik.carpooling.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telerik.carpooling.enums.PassengerStatus;
import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.models.base.MappedAudibleBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.*;

@EqualsAndHashCode(callSuper = true,exclude = {"comments"})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Audited
public class Trip extends MappedAudibleBase {

    private String origin;

    private String destination;

    @NotNull
    @Future
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime departureTime;

    @Column(nullable = false)
    @Range(min = 0,max = 8, message = "Please enter total number of seats between 0 and 8!")
    private Integer availablePlaces;

    private Integer tripDuration;

    private Integer costPerPassenger;

    private String message;

    private TripStatus tripStatus;

    @Size(min = 2,max = 3)
    private String smokingAllowed;

    @Size(min = 2,max = 3)
    private String luggageAllowed;

    @Size(min = 2,max = 3)
    private String petsAllowed;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver", nullable = false)
    private User driver;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car", nullable = false)
    private Car car;

    @OneToMany(mappedBy = "trip")
    @JsonIgnore
    @JsonIgnoreProperties("trip")
    private Set<Comment> comments = new HashSet<>();

    @JsonIgnore
    @ElementCollection
    @MapKeyColumn(name = "id")
    private Map<User, PassengerStatus> passengerStatus = new HashMap<>();
}

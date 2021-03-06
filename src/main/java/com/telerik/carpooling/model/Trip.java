package com.telerik.carpooling.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.model.base.MappedAudibleBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@OptimisticLocking(type = OptimisticLockType.DIRTY)
@DynamicUpdate
@Audited
public class Trip extends MappedAudibleBase {

    public static Trip NOT_FOUND = new Trip("No value",null,"No value",
            "No value",0, 0,0,
            true,true,true,true,TripStatus.AVAILABLE);

    @NotNull(message = "Trip should have message")
    @Size(max = 250,message = "Message should be maximum 250 symbols")
    private String message;

    @Future(message = "Trip departure time should be in the future")
    @NotNull(message = "Trip should have defined departure time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime departureTime;

    @NotNull(message = "Trip should have origin")
    @Size(max = 50,message = "Origin max size should be 50 symbols")
    private String origin;

    @NotNull(message = "Trip should have destination")
    @Size(max = 50,message = "Destination max size should be 50 symbols")
    private String destination;

    @NotNull(message = "Trip should have number of available places")
    @Range(min = 1,max = 4, message = "Please enter total number of seats between 1 and 4")
    private Integer availablePlaces;

    @NotNull(message = "Trip should have trip duration")
    @Max(value = Integer.MAX_VALUE,message = "Trip duration max value should be under 2 174 483 647")
    private Integer tripDuration;

    @NotNull(message = "Trip should have cost per passenger")
    @Max(value = Integer.MAX_VALUE,message = "Trip cost per passenger max value should be under 2 174 483 647")
    private Integer costPerPassenger;

    @NotNull(message = "Trip should have defined if smoking is allowed")
    private Boolean smokingAllowed;

    @NotNull(message = "Trip should have defined if luggage is allowed")
    private Boolean luggageAllowed;

    @NotNull(message = "Trip should have defined are pets allowed")
    private Boolean petsAllowed;

    @NotNull(message = "In trip, car should be marked as air-conditioned or not")
    private Boolean airConditioned;

    @NotNull(message = "Trip should have a trip status")
    private TripStatus tripStatus = TripStatus.AVAILABLE;
}

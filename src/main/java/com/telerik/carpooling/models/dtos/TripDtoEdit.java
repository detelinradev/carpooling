package com.telerik.carpooling.models.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TripDtoEdit {

    @NotNull(message = "Trip should have model id")
    private Long modelId;

    @NotNull(message = "Trip should have message")
    @Size(max = 250,message = "Message should be maximum 250 symbols")
    private String message;

    @NotNull(message = "Trip should have defined departure time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime departureTime;

    @NotNull(message = "Trip should have origin")
    @Size(max = 20)
    private String origin;

    @NotNull(message = "Trip should have destination")
    @Size(max = 20)
    private String destination;

    @NotNull(message = "Trip should have number of available places")
    @Range(min = 1,max = 8, message = "Please enter total number of seats between 1 and 8")
    private Integer availablePlaces;

    @NotNull(message = "Trip should have trip duration")
    @Max(value = Integer.MAX_VALUE,message = "Trip duration max value should be under 2 174 483 647")
    private Integer tripDuration;

    @NotNull(message = "Trip should have cost per passenger")
    @Max(value = Integer.MAX_VALUE,message = "Trip cost per passenger max value should be under 2 174 483 647")
    private Integer costPerPassenger;

    @NotNull(message = "Trip should have defined is smoking allowed")
    private Boolean smokingAllowed;

    @NotNull(message = "Trip should have defined is luggage allowed")
    private Boolean luggageAllowed;

    @NotNull(message = "Trip should have defined are pets allowed")
    private Boolean petsAllowed;
}

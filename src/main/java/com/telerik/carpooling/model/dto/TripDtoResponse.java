package com.telerik.carpooling.model.dto;

import com.telerik.carpooling.enums.TripStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TripDtoResponse {

    private Long modelId;
    private String message;
    private LocalDateTime departureTime;
    private String origin;
    private String destination;
    private Integer availablePlaces;
    private TripStatus tripStatus;
    private Integer tripDuration;
    private Integer costPerPassenger;
    private Boolean smokingAllowed;
    private Boolean luggageAllowed;
    private Boolean petsAllowed;
    private Boolean airConditioned;
}

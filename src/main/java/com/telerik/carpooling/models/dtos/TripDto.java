package com.telerik.carpooling.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TripDto {

    private String startingPoint;

    private String endPoint;

    private String departureTime;

    private String tripDuration;

    private String carModel;

    private boolean isAirConditioned;

    private boolean isSmokingAllowed;

    private boolean isLuggageAllowed;

    private boolean isPetsFriendly;

    private boolean isReturnJourney;

    private boolean AreChildrenUnder7Allowed;

    @Range(min = 1,max = 7)
    private int freeSeats;

    @Size(max = 200)
    private String message;

}

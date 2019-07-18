package com.telerik.carpooling.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TripDtoRequest {

    private String startingPoint;

    private String endPoint;

    private String departureDate;

    private String departureTime;

    private int tripDuration;

    private int costPerPassenger;

    @Size(max = 200)
    private String message;

}

package com.telerik.carpooling.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TripDtoRequest {

    @Size(max = 200)
    private String message;

    private String departureTime;

    private String origin;

    private String destination;

    private int availablePlaces;

    private int tripDuration;

    private int costPerPassenger;

}

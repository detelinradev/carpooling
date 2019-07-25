package com.telerik.carpooling.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TripDtoEdit {

    private int id;

    @Size(max = 200)
    private String message;

    @Size(max = 50)
    private String departureTime;

    @Size(max = 20)
    private String origin;

    @Size(max = 20)
    private String destination;

    @Range(min = 1,max = 8, message = "Please enter total number of seats between 1 and 8!")
    private int availablePlaces;

    @Max(value = Integer.MAX_VALUE)
    private int tripDuration;

    @Max(value = Integer.MAX_VALUE)
    private int costPerPassenger;
}

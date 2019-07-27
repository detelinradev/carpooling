package com.telerik.carpooling.models.dtos;

import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.models.Car;
import com.telerik.carpooling.models.Comment;
import com.telerik.carpooling.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.Size;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TripDtoResponse {
    private long id;

    private UserDtoResponse driver;

    @Size(max = 200)
    private String message;

    @Size(max = 50)
    private String departureTime;

    @Size(max = 20)
    private String origin;

    @Size(max = 20)
    private String destination;

    @Range(min = 1, max = 8, message = "Please enter total number of seats between 1 and 8!")
    private int availablePlaces;

    private Set<UserDtoResponse> passengers;

    private TripStatus tripStatus;

    private Set<CommentDtoResponse> comments;

    @Max(value = Integer.MAX_VALUE)
    private int tripDuration;

    @Max(value = Integer.MAX_VALUE)
    private int costPerPassenger;

    private CarDtoResponse car;
}

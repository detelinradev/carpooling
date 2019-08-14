package com.telerik.carpooling.models.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.telerik.carpooling.enums.PassengerStatus;
import com.telerik.carpooling.enums.TripStatus;
import com.telerik.carpooling.models.Car;
import com.telerik.carpooling.models.Comment;
import com.telerik.carpooling.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TripDtoResponse {
    private Long modelId;

    private UserDtoResponse driver;

    @Size(max = 200)
    private String message;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime departureTime;

    @Size(max = 20)
    private String origin;

    @Size(max = 20)
    private String destination;

    @Range(min = 1, max = 8, message = "Please enter total number of seats between 1 and 8!")
    private Integer availablePlaces;

    private Set<UserDtoResponse> passengers;

    private TripStatus tripStatus;

    private Set<CommentDtoResponse> comments;

    private Set<UserDtoResponse>notApprovedPassengers;

    private Map<UserDtoResponse, PassengerStatus> passengerStatus;

    @Max(value = Integer.MAX_VALUE)
    private Integer tripDuration;

    @Max(value = Integer.MAX_VALUE)
    private Integer costPerPassenger;

    private CarDtoResponse car;

    @Size(min = 2,max = 3)
    private String smokingAllowed;

    @Size(min = 2,max = 3)
    private String luggageAllowed;

    @Size(min = 2,max = 3)
    private String petsAllowed;
}

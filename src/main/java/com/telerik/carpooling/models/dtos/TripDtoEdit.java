package com.telerik.carpooling.models.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TripDtoEdit {

    private Long modelId;

    @Size(max = 200)
    private String message;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
//    @JsonFormat(pattern = "yyyy-mm-dd HH:MM")
    private LocalDateTime departureTime;

    @Size(max = 20)
    private String origin;

    @Size(max = 20)
    private String destination;

   // @Range(min = 1,max = 8, message = "Please enter total number of seats between 1 and 8!")
    private Integer availablePlaces;

    @Max(value = Integer.MAX_VALUE)
    private Integer tripDuration;

    @Max(value = Integer.MAX_VALUE)
    private Integer costPerPassenger;

    @Size(min = 2,max = 3)
    private String smokingAllowed;

    @Size(min = 2,max = 3)
    private String luggageAllowed;

    @Size(min = 2,max = 3)
    private String petsAllowed;
}

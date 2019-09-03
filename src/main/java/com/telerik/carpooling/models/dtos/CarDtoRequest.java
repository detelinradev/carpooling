package com.telerik.carpooling.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Calendar;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CarDtoRequest {

    @NotNull(message = "Car should have brand")
    @Size(min = 1, max = 20, message = "Please enter brand name between 1 and 20 symbols!")
    private String brand;

    @NotNull(message = "Car should have model")
    @Size(min = 1, max = 20, message = "Please enter model name between 1 and 20 symbols!")
    private String model;

    @NotNull(message = "Car should have color")
    @Size(min = 3,max = 20)
    private String color;

    @NotNull(message = "Car should have year of first registration")
    private Integer firstRegistration;

    @NotNull(message = "Car should be marked as air-conditioned or not")
    private Boolean airConditioned;

}

package com.telerik.carpooling.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CarDtoEdit {

    @NotNull(message = "Car should have id")
    private Long modelId;

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
}

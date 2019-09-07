package com.telerik.carpooling.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class CarDtoResponse {

    private Long modelId;
    private String brand;
    private String model;
    private String color;
    private Integer firstRegistration;
}

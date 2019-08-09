package com.telerik.carpooling.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import javax.persistence.Column;
import javax.validation.constraints.Size;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class CarDtoResponse {

    private Long modelId;

    @Size(min = 1, max = 20, message = "Please enter brand name between 1 and 20 symbols!")
    private String brand;

    @Size(min = 1, max = 20, message = "Please enter model name between 1 and 20 symbols!")
    private String model;

    @Size(min = 3,max = 20)
    private String color;

    @Column(nullable = false)
    @Range(min = 1950,max = 2019, message = "Please enter year of first registration between 1950 and 2019!")
    private Integer firstRegistration;

    @Size(min = 2,max = 3)
    private String airConditioned;

    @Size(max = 250)
    private String avatarUri;
}

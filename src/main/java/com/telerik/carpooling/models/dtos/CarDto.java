package com.telerik.carpooling.models.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telerik.carpooling.models.Image;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.persistence.OneToOne;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CarDto {

    @Size(min = 1, max = 20, message = "Please enter brand name between 1 and 20 symbols")
    private String brand;

    @Size(min = 1, max = 20, message = "Please enter model name between 1 and 20 symbols")
    private String model;

    private String color;

    @Column(nullable = false)
    @Range(min = 1,max = 8)
    private int totalSeats;

    @Column(nullable = false)
    @Range(min = 1950,max = 2019)
    private int firstRegistration;

    @Column(nullable = false)
    private boolean isAirConditioned;

    @Column(nullable = false)
    private boolean isSmokingAllowed;

    @Column(nullable = false)
    private boolean isLuggageAllowed;

    @Column(nullable = false)
    private boolean isPetsAllowed;

    @OneToOne(mappedBy = "car")
    @JsonIgnore
    private Image carImage;
}

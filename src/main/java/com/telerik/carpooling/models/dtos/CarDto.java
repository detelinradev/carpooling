package com.telerik.carpooling.models.dtos;


import com.telerik.carpooling.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
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

    @Column
    private String color;

    @Column(nullable = false)
    private int totalSeats;

    @Column(nullable = false)
    private int firstRegistration;

    @Column
    private boolean isAirConditioned;

    @Column
    private boolean isSmokingAllowed;

    @Column
    private boolean isLuggageAllowed;

    @Column
    private boolean isPetsAllowed;

    @Column
    private boolean isReturnJourney;

    @Column
    private boolean AreChildrenUnder7Allowed;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user", unique = true)
    private User owner;
}

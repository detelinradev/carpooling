package com.telerik.carpooling.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telerik.carpooling.models.base.MappedAudibleBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Car extends MappedAudibleBase {


    @Size(min = 1, max = 20, message = "Please enter brand name between 1 and 20 symbols!")
    private String brand;

    @Size(min = 1, max = 20, message = "Please enter model name between 1 and 20 symbols!")
    private String model;

    private String color;

    @Column(nullable = false)
    @Range(min = 1,max = 8, message = "Please enter total number of seats between 1 and 8!")
    private int totalSeats;

    @Column(nullable = false)
    @Range(min = 1950,max = 2019, message = "Please enter year of first registration between 1950 and 2019!")
    private int firstRegistration;

    @Column(nullable = false)
    private boolean isAirConditioned;

    @Column(nullable = false)
    private boolean isSmokingAllowed;

    @Column(nullable = false)
    private boolean isLuggageAllowed;

    @Column(nullable = false)
    private boolean isPetsAllowed;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user", unique = true)
    private User owner;

    @OneToOne(mappedBy = "car")
    @JsonIgnore
    private Image carImage;
}

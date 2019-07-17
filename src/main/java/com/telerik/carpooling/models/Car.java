package com.telerik.carpooling.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telerik.carpooling.models.base.MappedAudibleBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Car extends MappedAudibleBase {

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
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

    @OneToOne(mappedBy = "car")
    @JsonIgnore
    private Image carImage;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user", unique = true)
    private User owner;
}

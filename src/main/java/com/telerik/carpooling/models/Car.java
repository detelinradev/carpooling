package com.telerik.carpooling.models;

import com.telerik.carpooling.models.base.MappedAudibleBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Audited
public class Car extends MappedAudibleBase {

    public static Car NOT_FOUND = new Car("No value","No value","No value",0,true,
            User.NOT_FOUND);

    @NotNull(message = "Car should have brand")
    @Size(min = 1, max = 20, message = "Car brand should be between 1 and 20 symbols")
    private String brand;

    @NotNull(message = "Car should have model")
    @Size(min = 1, max = 20, message = "Car model should be between 1 and 20 symbols")
    private String model;

    @NotNull(message = "Car should have color")
    @Size(min = 3,max = 20, message = "Car's color name should be between 1 and 20 symbols")
    private String color;

    @NotNull(message = "Car should have year of first registration")
    private Integer firstRegistration;

    @NotNull(message = "In trip, car should be marked as air-conditioned or not")
    private Boolean airConditioned;

    @NotNull(message = "Car should have owner")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user", unique = true)
    private User owner;
}

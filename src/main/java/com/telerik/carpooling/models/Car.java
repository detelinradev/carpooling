package com.telerik.carpooling.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telerik.carpooling.models.base.MappedAudibleBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true,exclude = {"carImage", "owner"})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Audited
public class Car extends MappedAudibleBase {

    @Column(nullable = false)
    @Size(min = 1, max = 20, message = "Please enter brand name between 1 and 20 symbols!")
    private String brand;

    @Column(nullable = false)
    @Size(min = 1, max = 20, message = "Please enter model name between 1 and 20 symbols!")
    private String model;

    private String color;

    @Column(nullable = false)
    @Range(min = 1950,max = 2019, message = "Please enter year of first registration between 1950 and 2019!")
    private Integer firstRegistration;

    @Column(nullable = false)
    private Boolean isAirConditioned;

    @Column(nullable = false)
    private Boolean isSmokingAllowed;

    @Column(nullable = false)
    private Boolean isLuggageAllowed;

    @Column(nullable = false)
    private Boolean isPetsAllowed;

    @OneToOne(mappedBy = "car")
    @JsonIgnoreProperties("car")
    private Image carImage;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user", unique = true)
    @JsonIgnoreProperties("car")
    private User owner;

    private String avatarUri;
}

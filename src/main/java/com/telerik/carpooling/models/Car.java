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

    @OneToOne(mappedBy = "car")
    @JsonIgnoreProperties("car")
    private Image carImage;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user", unique = true)
    @JsonIgnoreProperties("car")
    private User owner;

    @Size(max = 250)
    private String avatarUri;
}

package com.telerik.carpooling.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telerik.carpooling.models.base.MappedAudibleBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;


@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class User extends MappedAudibleBase {

    @Column(updatable = false, nullable = false)
    @Size(min = 1, max = 20, message = "Please enter first name between 1 and 20 characters")
    @Pattern(regexp = "^[A-Za-z]+$",message = "First name must contain only letters")
    private String firstName;

    @Size(min = 1, max = 20, message = "Please enter last name between 1 and 20 characters")
    @Column(updatable = false, nullable = false)
    @Pattern(regexp = "^[A-Za-z]+$",message = "Last name must contain only letters")
    private String lastName;

    @Size(min = 2, max = 20, message = "Please enter username between 2 and 20 symbols")
    @Column(unique = true, nullable = false,updatable = false )
    private String username;

    @Email
    private String email;

    @Min(value = 18,message = "Not of age persons are not allowed to use the carpooling service")
    @Max(value = 150,message = "Please enter valid years")
    private int age;

    @Size(min = 8, max = 128, message = "Password must be between 8 and 128 symbols")
    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(nullable = false)
    private String role;

    private int countRatingsAsDriver;

    @Range(max = 5)
    private double averageRatingDriver;

    private int countRatingsAsPassenger;

    @Range(max = 5)
    private double averageRatingPassenger;

    @OneToOne(mappedBy = "user")
    @JsonIgnore
    private Image userImage;

    @OneToOne(mappedBy = "owner")
    @JsonIgnore
    private Car car;

//    @JsonIgnore
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "driver")
//    private Set<Trip> tripsAsDriver = new HashSet<>();

//    @JsonIgnore
//    @ManyToMany(mappedBy = "acceptedPassengers")
//    private Set<Trip> tripsAsPassenger = new HashSet<>();


}

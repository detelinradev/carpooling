package com.telerik.carpooling.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telerik.carpooling.models.base.MappedAudibleBase;
import lombok.*;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.*;


@EqualsAndHashCode(callSuper = true, exclude = {"userImage", "car"})
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Audited
public class User extends MappedAudibleBase {

    public static User NOT_FOUND = new User("No value", "No value", "No value",
            "No value", "No value", "No value", "No value", null,
            null, Image.NOT_FOUND, Car.NOT_FOUND);

    @NotNull(message = "User should have username")
    @Size(min = 2, max = 20, message = "Please enter username between 2 and 20 symbols")
    @Column(unique = true, updatable = false)
    private String username;

    @NotNull(message = "User should have first name")
    @Pattern(regexp = "^([A-Za-z]*).{1,20}$", message = "First name should contain only letters," +
            " and should be between 1 and 20 characters")
    @Column(updatable = false)
    private String firstName;

    @NotNull(message = "User should have last name")
    @Pattern(regexp = "^([A-Za-z]*).{1,20}$", message = "Last name should contain only letters," +
            " and should be between 1 and 20 characters")
    @Column(updatable = false)
    private String lastName;

    @NotNull(message = "User should have email")
    @Email(message = "Email should follow the pattern xxxx@yyyy.zzzz")
    private String email;

    @NotNull(message = "User should have user role")
    private String role;

    @JsonIgnore
    @NotNull(message = "User should have password")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,128}$",
            message = "Please enter password that contains:\n" +
                    "\n" +
                    "At least one upper case English letter,\n" +
                    "At least one lower case English letter,\n" +
                    "At least one digit,\n" +
                    "At least one special character,\n" +
                    "Minimum eight in length and maximum 128")
    private String password;

    @NotNull(message = "User should have phone")
    @Pattern(regexp = "^08[789]\\d{7}$", message = "Phone should consist only of digits," +
            "starts with 0, and should be 10 symbols long")
    private String phone;

    @NotNull(message = "User should have rating as driver")
    @DecimalMax(value = "5.00",message = "Rating as driver should be between 0 and 5")
    @DecimalMin(value = "0.00",message = "Rating as driver should be between 0 and 5")
    private Double ratingAsDriver;

    @NotNull(message = "User should have rating as passenger")
    @DecimalMax(value = "5.00",message = "Rating as passenger should be between 0 and 5")
    @DecimalMin(value = "0.00",message = "Rating as passenger should be between 0 and 5")
    private Double ratingAsPassenger;

    @JsonIgnore
    @OneToOne(mappedBy = "user")
    @JsonIgnoreProperties("user")
    private Image userImage;

    @JsonIgnore
    @OneToOne(mappedBy = "owner")
    @JsonIgnoreProperties("owner")
    private Car car;
}

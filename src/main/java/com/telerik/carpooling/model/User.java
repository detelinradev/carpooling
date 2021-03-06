package com.telerik.carpooling.model;

import com.telerik.carpooling.enums.UserRole;
import com.telerik.carpooling.model.base.MappedAudibleBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.*;


@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@OptimisticLocking(type = OptimisticLockType.DIRTY)
@DynamicUpdate
@Audited
public class User extends MappedAudibleBase {

    public static User NOT_FOUND = new User("No value", "No value", "No value",
            "No value", UserRole.USER, "No value", "No value", 0.0,
            0.0,0,0.0,0,0.0);

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
    private UserRole role = UserRole.USER;

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
    @DecimalMax(value = "5.00", message = "Rating as driver should be between 0 and 5")
    @DecimalMin(value = "0.00", message = "Rating as driver should be between 0 and 5")
    private Double ratingAsDriver = 0.0;

    @NotNull(message = "User should have rating as passenger")
    @DecimalMax(value = "5.00", message = "Rating as passenger should be between 0 and 5")
    @DecimalMin(value = "0.00", message = "Rating as passenger should be between 0 and 5")
    private Double ratingAsPassenger = 0.0;

    private Integer countRatingsAsPassenger = 0;

    private Double sumRatingsAsPassenger = 0.0;

    private Integer countRatingsAsDriver = 0;

    private Double sumRatingsAsDriver = 0.0;
}

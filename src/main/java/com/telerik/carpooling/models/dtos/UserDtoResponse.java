package com.telerik.carpooling.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.validation.constraints.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDtoResponse {

    @Column
    private Long id;

    @Size(min = 2, max = 20, message = "Please enter username between 2 and 20 symbols")
    @Column(unique = true, nullable = false, updatable = false)
    private String username;

    @Column(updatable = false, nullable = false)
    @Size(min = 1, max = 20, message = "Please enter first name between 1 and 20 characters")
    @Pattern(regexp = "^[A-Za-z]+$", message = "First name must contain only letters")
    private String firstName;

    @Size(min = 1, max = 20, message = "Please enter last name between 1 and 20 characters")
    @Column(updatable = false, nullable = false)
    @Pattern(regexp = "^[A-Za-z]+$", message = "Last name must contain only letters")
    private String lastName;

    @Email
    private String email;

    @Range(min = 10, max = 10)
    private String phone;

    private Double ratingAsDriver;

    private Double ratingAsPassenger;

    @Size(max = 250)
    private String avatarUri;

}

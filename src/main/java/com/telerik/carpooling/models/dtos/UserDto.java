package com.telerik.carpooling.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto {
    @Size(min = 1, max = 20, message = "Please enter first name between 1 and 20 symbols")
    @Pattern(regexp = "^[A-Za-z]+$",message = "Please enter first name that contains only letters")
    private String firstName;

    @Size(min = 1, max = 20, message = "Please enter last name between 1 and 20 symbols")
    @Pattern(regexp = "^[A-Za-z]+$",message = "Please enter last name that contains only letters")
    private String lastName;

    @Size(min = 2, max = 20, message = "Please enter username between 2 and 20 symbols")
    private String username;

    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,128}$",
            message = "Please enter password that contains:\n" +
            "\n" +
            "At least one upper case English letter,\n" +
            "At least one lower case English letter,\n" +
            "At least one digit,\n" +
            "At least one special character,\n" +
            "Minimum eight in length and maximum 128")
    private String password;

    @Range(min = 18,max = 150)
    private int age;

    @Email
    private String email;

}

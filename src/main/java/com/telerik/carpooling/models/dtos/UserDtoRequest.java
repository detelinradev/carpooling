package com.telerik.carpooling.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDtoRequest {

    @NotNull(message = "User should have username")
    @Size(min = 2, max = 20, message = "Please enter username between 2 and 20 symbols")
    private String username;

    @NotNull(message = "User should have first name")
    @Pattern(regexp = "^([A-Za-z]*).{1,20}$", message = "First name should contain only letters," +
            " and should be between 1 and 20 characters")
    private String firstName;

    @NotNull(message = "User should have last name")
    @Pattern(regexp = "^([A-Za-z]*).{1,20}$", message = "Last name should contain only letters," +
            " and should be between 1 and 20 characters")
    private String lastName;

    @NotNull(message = "User should have email")
    @Email(message = "Email should follow the pattern xxxx@yyyy.zzzz")
    private String email;

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
    @Pattern(regexp="^08[789]\\d{7}$", message = "Phone should consist only of digits," +
            "starts with 0, and should be 10 symbols long")
    private String phone;

}

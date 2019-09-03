package com.telerik.carpooling.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDtoEdit {

    @NotNull(message = "User should have model id")
    private Long modelId;

    @NotNull(message = "User should have email")
    @Email(message = "Email should follow the pattern xxxx@yyyy.zzzz")
    private String email;

    @NotNull(message = "User should have user role")
    private String role;

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

package com.telerik.carpooling.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDtoResponse {

    private Long modelId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String phone;
    private Double ratingAsDriver;
    private Double ratingAsPassenger;
    private CarDtoResponse car;
}

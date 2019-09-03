package com.telerik.carpooling.models.dtos;

import com.telerik.carpooling.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TripUserStatusDtoResponse {

    private Long modelId;
    private UserDtoResponse user;
    private UserStatus userStatus;
}

package com.telerik.carpooling.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum PassengerStatus {
    PENDING("P"), ACCEPTED("AC"), REJECTED("R"), CANCELED("C"), ABSENT("AB");

    private String code;

}

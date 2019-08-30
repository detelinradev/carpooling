package com.telerik.carpooling.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum PassengerStatus {
    PENDING("A"), ACCEPTED("B"), REJECTED("C"),
    CANCELED("D"), ABSENT("E"), DRIVER("F");

    private String code;

}

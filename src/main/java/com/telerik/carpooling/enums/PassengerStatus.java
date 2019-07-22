package com.telerik.carpooling.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum PassengerStatus {
    PENDING("PENDING"), ACCEPTED("ACCEPTED"), REJECTED("REJECTED"),
    CANCELED("CANCELED"), ABSENT("ABSENT");

    private String code;

}

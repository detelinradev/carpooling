package com.telerik.carpooling.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TripStatus {
    AVAILABLE("A"), BOOKED("B"), ONGOING("C"),
    DONE("D"), CANCELED("E");

    private String code;

}

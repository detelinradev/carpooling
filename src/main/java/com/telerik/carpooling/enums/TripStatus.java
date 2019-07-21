package com.telerik.carpooling.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TripStatus {
    AVAILABLE("A"), BOOKED("B"), ONGOING("O"), DONE("D"), CANCELED("C");

    private String code;

}

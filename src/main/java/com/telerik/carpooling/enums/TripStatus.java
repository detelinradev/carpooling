package com.telerik.carpooling.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TripStatus {
    AVAILABLE("AVAILABLE"), BOOKED("BOOKED"), ONGOING("ONGOING"),
    DONE("DONE"), CANCELED("CANCELED");

    private String code;

}

package com.telerik.carpooling.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

//@Getter
//@AllArgsConstructor
//@NoArgsConstructor
public enum PassengerStatus {
    PENDING("P"), ACCEPTED("A"), REJECTED("R"), CANCELED("C"), ABSENT("A");

    private String code;

    PassengerStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

package com.telerik.carpooling.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum UserStatus {
    PENDING("A"), ACCEPTED("B"), REJECTED("C"),
    CANCELED("D"), ABSENT("E"), DRIVER("F");

    private final String code;

}

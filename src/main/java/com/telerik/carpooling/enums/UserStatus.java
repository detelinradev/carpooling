package com.telerik.carpooling.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum UserStatus {
    PENDING("A"), ACCEPTED("B"), REJECTED("C"),
    CANCELED("D"), ABSENT("E");

    private String code;

}

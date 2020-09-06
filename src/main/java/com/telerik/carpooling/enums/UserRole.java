package com.telerik.carpooling.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum UserRole {
    ADMIN("A"), USER("B");

    private final String code;
}

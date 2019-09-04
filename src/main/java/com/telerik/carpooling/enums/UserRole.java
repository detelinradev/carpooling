package com.telerik.carpooling.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {
    ADMIN("A"), USER("B");

    private String code;
}

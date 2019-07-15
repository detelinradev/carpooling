package com.telerik.carpooling.security;


import lombok.Setter;

@Setter
public class AccountCredentials {

    private String username;
    private String password;

    public AccountCredentials() {
    }

    String getUsername() {
        return username;
    }
    String getPassword() {
        return password;
    }

}


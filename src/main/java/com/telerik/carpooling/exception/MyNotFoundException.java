package com.telerik.carpooling.exception;


public class MyNotFoundException extends Exception {

    public MyNotFoundException(String message) {
        super(message);
    }

    public MyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}

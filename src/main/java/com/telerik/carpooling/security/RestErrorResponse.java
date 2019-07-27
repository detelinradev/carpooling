//package com.telerik.carpooling.security;
//
//import lombok.Data;
//import org.springframework.http.HttpStatus;
//
//@Data
//public class RestErrorResponse {
//
//    private final int statusCode;
//    private final String reasonPhrase;
//    private final String detailMessage;
//
//    protected RestErrorResponse(HttpStatus status, String detailMessage) {
//        statusCode = status.value();
//        reasonPhrase = status.getReasonPhrase();
//        this.detailMessage = detailMessage;
//    }
//
//    public static RestErrorResponse of(HttpStatus status) {
//        return of(status, null);
//    }
//
//    public static RestErrorResponse of(HttpStatus status, Exception ex) {
//        return new RestErrorResponse(status, ex.getMessage());
//    }
//
//}

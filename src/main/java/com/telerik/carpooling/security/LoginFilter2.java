//package com.telerik.carpooling.security;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.InternalAuthenticationServiceException;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@RequiredArgsConstructor
//public class LoginFilter extends UsernamePasswordAuthenticationFilter {
//
//    private static final String LOGIN_REQUEST_ATTRIBUTE = "login_request";
//    public static final String REMEMBER_ME_ATTRIBUTE = "remember_me";
//    private final ObjectMapper objectMapper;
//
//
//    @Override
//    public Authentication attemptAuthentication(
//            HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//
//        try {
//            LoginRequest loginRequest =
//                    objectMapper.readValue(request.getInputStream(), LoginRequest.class);
//
//            request.setAttribute(LOGIN_REQUEST_ATTRIBUTE, loginRequest);
//
//            return super.attemptAuthentication(request, response);
//        } catch (IOException ioe) {
//            throw new InternalAuthenticationServiceException(ioe.getMessage(), ioe);
//        } finally {
//            request.removeAttribute(LOGIN_REQUEST_ATTRIBUTE);
//        }
//    }
//
//    @Override
//    protected String obtainUsername(HttpServletRequest request) {
//        return toLoginRequest(request).getUsername();
//    }
//
//    @Override
//    protected String obtainPassword(HttpServletRequest request) {
//        return toLoginRequest(request).getPassword();
//    }
//
//    private LoginRequest toLoginRequest(HttpServletRequest request) {
//        return (LoginRequest)request.getAttribute(LOGIN_REQUEST_ATTRIBUTE);
//    }
//
//}

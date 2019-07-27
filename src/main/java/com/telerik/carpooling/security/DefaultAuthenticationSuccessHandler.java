//package com.telerik.carpooling.security;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
//
//public class DefaultAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
//
//    private static final int ONE_DAY_MINUTES = 24 * 60;
//
//    private final JwtTokenService jwtTokenService;
//    private final ObjectMapper objectMapper;
//
//    public DefaultAuthenticationSuccessHandler(
//            JwtTokenService jwtTokenService, ObjectMapper objectMapper) {
//
//        this.jwtTokenService = jwtTokenService;
//        this.objectMapper = objectMapper;
//    }
//
//    @Override
//    public void onAuthenticationSuccess(
//            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
//            throws IOException {
//
//        response.setContentType(APPLICATION_JSON_VALUE);
//
//        String jwtToken = jwtTokenService.createJwtToken(authentication, ONE_DAY_MINUTES);
//        objectMapper.writeValue(response.getWriter(), jwtToken);
//    }
//
//}

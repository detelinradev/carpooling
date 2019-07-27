//package com.telerik.carpooling.security;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.mariadb.jdbc.internal.logging.Logger;
//import org.mariadb.jdbc.internal.logging.LoggerFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.Writer;
//
//import static org.springframework.http.HttpStatus.UNAUTHORIZED;
//import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
//
//public class DefaultAuthenticationFailureHandler implements AuthenticationFailureHandler {
//
//    private static final Logger LOGGER =
//            LoggerFactory.getLogger(DefaultAuthenticationFailureHandler.class);
//
//    private final ObjectMapper objectMapper;
//
//    public DefaultAuthenticationFailureHandler(ObjectMapper objectMapper) {
//        this.objectMapper = objectMapper;
//    }
//
//    @Override
//    public void onAuthenticationFailure(
//            HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
//            throws IOException {
//
//        LOGGER.warn(exception.getMessage());
//
//        HttpStatus httpStatus = translateAuthenticationException(exception);
//
//        response.setStatus(httpStatus.value());
//        response.setContentType(APPLICATION_JSON_VALUE);
//
//        writeResponse(response.getWriter(), httpStatus, exception);
//    }
//
//    protected HttpStatus translateAuthenticationException(AuthenticationException exception) {
//        return UNAUTHORIZED;
//    }
//
//    protected void writeResponse(
//            Writer writer, HttpStatus httpStatus, AuthenticationException exception) throws IOException {
//
//        RestErrorResponse restErrorResponse = RestErrorResponse.of(httpStatus, exception);
//        objectMapper.writeValue(writer, restErrorResponse);
//    }
//
//}

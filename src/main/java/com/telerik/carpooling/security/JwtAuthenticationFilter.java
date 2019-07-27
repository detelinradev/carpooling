//package com.telerik.carpooling.security;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.util.StringUtils;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Log4j2
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//
//    private static final String AUTHORIZATION_HEADER = "Authorization";
//    private static final String TOKEN_PREFIX = "Bearer";
//
//    private final JwtTokenService jwtTokenService;
//
//    public JwtAuthenticationFilter(JwtTokenService jwtTokenService) {
//        this.jwtTokenService = jwtTokenService;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//
//        Authentication authentication = getAuthentication(request);
//        if (authentication == null) {
//            SecurityContextHolder.clearContext();
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        try {
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            filterChain.doFilter(request, response);
//        } finally {
//            SecurityContextHolder.clearContext();
//        }
//    }
//
//    private Authentication getAuthentication(HttpServletRequest request) {
//        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
//        if (StringUtils.isEmpty(authorizationHeader)) {
//            log.debug("Authorization header is empty.");
//            return null;
//        }
//
//        if (StringUtils.substringMatch(authorizationHeader, 0, TOKEN_PREFIX)) {
//            log.debug("Token prefix {} in Authorization header was not found.", TOKEN_PREFIX);
//            return null;
//        }
//
//        String jwtToken = authorizationHeader.substring(TOKEN_PREFIX.length() + 1);
//
//        try {
//            return jwtTokenService.parseJwtToken(jwtToken);
//        } catch (AuthenticationException e) {
//            log.warn(e.getMessage());
//            return null;
//        }
//    }
//
//}
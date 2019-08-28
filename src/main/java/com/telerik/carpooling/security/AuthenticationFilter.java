package com.telerik.carpooling.security;


import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;


public class AuthenticationFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        try {
            Authentication authentication = AuthenticationService.getAuthentication((HttpServletRequest) request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch (ExpiredJwtException ex){
            System.out.println();
        }

        filterChain.doFilter(request, response);
    }
}

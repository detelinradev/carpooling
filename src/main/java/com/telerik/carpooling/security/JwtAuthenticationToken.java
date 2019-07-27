//package com.telerik.carpooling.security;
//
//import io.jsonwebtoken.Claims;
//import org.springframework.security.authentication.AbstractAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.Date;
//import java.util.stream.Collectors;
//
//public class JwtAuthenticationToken extends AbstractAuthenticationToken {
//
//    private static final String AUTHORITIES = "authorities";
//
//    private final long userId;
//
//    private JwtAuthenticationToken(long userId, Collection<? extends GrantedAuthority> authorities) {
//        super(authorities);
//        this.userId = userId;
//    }
//
//    @Override
//    public Object getCredentials() {
//        return null;
//    }
//
//    @Override
//    public Long getPrincipal() {
//        return userId;
//    }
//
//    public static JwtAuthenticationToken of(Claims claims) {
//        long userId = Long.parseLong(claims.getSubject());
//
//        Collection<GrantedAuthority> authorities =
//                Arrays.stream(String.valueOf(claims.get(AUTHORITIES)).split(","))
//                        .map(String::trim)
//                        .map(String::toUpperCase)
//                        .map(SimpleGrantedAuthority::new)
//                        .collect(Collectors.toSet());
//
//        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(userId, authorities);
//
//        Date now = new Date();
//        Date expiration = claims.getExpiration();
//        Date notBefore = claims.getNotBefore();
//        jwtAuthenticationToken.setAuthenticated(now.after(notBefore) && now.before(expiration));
//
//        return jwtAuthenticationToken;
//    }
//
//}

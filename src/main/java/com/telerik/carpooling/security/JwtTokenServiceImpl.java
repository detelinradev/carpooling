//package com.telerik.carpooling.security;
//
//import io.jsonwebtoken.*;
//import org.springframework.security.authentication.AuthenticationServiceException;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.InternalAuthenticationServiceException;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.GrantedAuthority;
//
//import java.util.Date;
//import java.util.stream.Collectors;
//
//import static io.jsonwebtoken.SignatureAlgorithm.HS512;
//import static java.lang.System.currentTimeMillis;
//
//public class JwtTokenServiceImpl implements JwtTokenService {
//
//    private static final String AUTHORITIES = "authorities";
//
//    static final String SECRET = "ThisIsASecret";
//
//    @Override
//    public String createJwtToken(Authentication authentication, int minutes) {
//        Claims claims = Jwts.claims()
//                .setId(String.valueOf(IdentityGenerator.generate()))
//                .setSubject(authentication.getName())
//                .setExpiration(new Date(currentTimeMillis() + minutes * 60 * 1000))
//                .setIssuedAt(new Date());
//
//        String authorities = authentication.getAuthorities()
//                .stream()
//                .map(GrantedAuthority::getAuthority)
//                .map(String::toUpperCase)
//                .collect(Collectors.joining(","));
//
//        claims.put(AUTHORITIES, authorities);
//
//        return Jwts.builder()
//                .setClaims(claims)
//                .signWith(HS512, SECRET)
//                .compact();
//    }
//
//    @Override
//    public Authentication parseJwtToken(String jwtToken) throws AuthenticationException {
//        try {
//            Claims claims = Jwts.parser()
//                    .setSigningKey(SECRET)
//                    .parseClaimsJws(jwtToken)
//                    .getBody();
//
//            return JwtAuthenticationToken.of(claims);
//        } catch (ExpiredJwtException | SignatureException e) {
//            throw new BadCredentialsException(e.getMessage(), e);
//        } catch (UnsupportedJwtException | MalformedJwtException e) {
//            throw new AuthenticationServiceException(e.getMessage(), e);
//        } catch (IllegalArgumentException e) {
//            throw new InternalAuthenticationServiceException(e.getMessage(), e);
//        }
//    }
//
//}

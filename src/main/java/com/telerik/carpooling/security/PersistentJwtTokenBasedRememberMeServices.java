//package com.telerik.carpooling.security;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.JwtException;
//import io.jsonwebtoken.Jwts;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
//import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
//import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.Date;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//import static com.telerik.carpooling.security.LoginFilter.REMEMBER_ME_ATTRIBUTE;
//import static io.jsonwebtoken.SignatureAlgorithm.HS512;
//import static java.lang.System.currentTimeMillis;
//
//@Log4j2
//
//public class PersistentJwtTokenBasedRememberMeServices extends
//        PersistentTokenBasedRememberMeServices {
//
//
//    public static final int DEFAULT_TOKEN_LENGTH = 16;
//
//    public PersistentJwtTokenBasedRememberMeServices(
//            String key, UserDetailsService userDetailsService,
//            PersistentTokenRepository tokenRepository) {
//
//        super(key, userDetailsService, tokenRepository);
//    }
//
//    @Override
//    protected String[] decodeCookie(String cookieValue) throws InvalidCookieException {
//        try {
//            Claims claims = Jwts.parser()
//                    .setSigningKey(getKey())
//                    .parseClaimsJws(cookieValue)
//                    .getBody();
//
//            return new String[]{claims.getId(), claims.getSubject()};
//        } catch (JwtException e) {
//            log.warn(e.getMessage());
//            throw new InvalidCookieException(e.getMessage());
//        }
//    }
//
//    @Override
//    protected String encodeCookie(String[] cookieTokens) {
//        Claims claims = Jwts.claims()
//                .setId(cookieTokens[0])
//                .setSubject(cookieTokens[1])
//                .setExpiration(new Date(currentTimeMillis() + getTokenValiditySeconds() * 1000L))
//                .setIssuedAt(new Date());
//
//        return Jwts.builder()
//                .setClaims(claims)
//                .signWith(HS512, getKey())
//                .compact();
//    }
//
//    @Override
//    protected String generateSeriesData() {
//        long seriesId = IdentityGenerator.generate();
//        return String.valueOf(seriesId);
//    }
//
//    @Override
//    protected String generateTokenData() {
//        return RandomUtil.ints(DEFAULT_TOKEN_LENGTH)
//                .mapToObj(i -> String.format("%04x", i))
//                .collect(Collectors.joining());
//    }
//
//    @Override
//    protected boolean rememberMeRequested(HttpServletRequest request, String parameter) {
//        return Optional.ofNullable((Boolean) request.getAttribute(REMEMBER_ME_ATTRIBUTE)).orElse(false);
//    }
//
//}
//

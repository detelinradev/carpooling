//package com.telerik.carpooling.security;
//
//import com.telerik.carpooling.exceptions.NoSuchSessionException;
//import com.telerik.carpooling.models.Session;
//import com.telerik.carpooling.services.services.contracts.SessionService;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
//import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
//
//import java.time.LocalDateTime;
//import java.util.Date;
//import java.util.Optional;
//
//import static com.telerik.carpooling.security.DateTimeUtil.toDate;
//import static com.telerik.carpooling.security.DateTimeUtil.toLocalDateTime;
//
//@Log4j2
//public class DelegatingPersistentTokenRepository implements PersistentTokenRepository {
//
//
//    private final SessionService sessionService;
//
//    public DelegatingPersistentTokenRepository(SessionService sessionService) {
//        this.sessionService = sessionService;
//    }
//
//    @Override
//    public void createNewToken(PersistentRememberMeToken token) {
//        Long sessionId = Long.valueOf(token.getSeries());
//        Long userId = Long.valueOf(token.getUsername());
//        sessionService.createSession(sessionId, userId, token.getTokenValue());
//    }
//
//    @Override
//    public void updateToken(String series, String tokenValue, Date lastUsed) {
//        Long sessionId = Long.valueOf(series);
//        try {
//            sessionService.useSession(sessionId, tokenValue, toLocalDateTime(lastUsed));
//        } catch (NoSuchSessionException e) {
//            log.warn("Session {} doesn't exists.", sessionId);
//        }
//    }
//
//    @Override
//    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
//        Long sessionId = Long.valueOf(seriesId);
//        return sessionService
//                .findSession(sessionId)
//                .map(this::toPersistentRememberMeToken)
//                .orElse(null);
//    }
//
//    @Override
//    public void removeUserTokens(String username) {
//        Long userId = Long.valueOf(username);
//        sessionService.logoutUser(userId);
//    }
//
//    private PersistentRememberMeToken toPersistentRememberMeToken(Session session) {
//        String username = String.valueOf(session.getUserId());
//        String series = String.valueOf(session.getId());
//        LocalDateTime lastUsedAt =
//                Optional.ofNullable(session.getLastUsedAt()).orElseGet(session::getIssuedAt);
//        return new PersistentRememberMeToken(
//                username, series, session.getToken(), toDate(lastUsedAt));
//    }
//
//}

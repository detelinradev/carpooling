//package com.telerik.carpooling.services;
//
//import com.telerik.carpooling.exceptions.NoSuchSessionException;
//import com.telerik.carpooling.repositories.SessionRepository;
//import com.telerik.carpooling.models.Session;
//import com.telerik.carpooling.security.UserEvent;
//import com.telerik.carpooling.security.UserEventEmitter;
//import com.telerik.carpooling.services.services.contracts.SessionService;
//import lombok.extern.log4j.Log4j2;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Objects;
//import java.util.Optional;
//
//import static com.telerik.carpooling.security.UserEventType.LOGGED_OUT;
//import static com.telerik.carpooling.security.UserEventType.SIGNIN_REMEMBER_ME;
//import static java.util.stream.Collectors.toList;
//
//@Log4j2
//public class SessionServiceImpl implements SessionService {
//
//
//    private final SessionRepository sessionRepository;
//    private final UserEventEmitter userEventEmitter;
//
//    public SessionServiceImpl(
//            SessionRepository sessionRepository, UserEventEmitter userEventEmitter) {
//
//        this.sessionRepository = sessionRepository;
//        this.userEventEmitter = userEventEmitter;
//    }
//
//    @Override
//    public Session createSession(long sessionId, long userId, String token) {
//        return createSession(sessionId, userId, token, 0);
//    }
//
//    @Override
//    public Session createSession(long sessionId, long userId, String token, int minutes) {
//        Objects.requireNonNull(userId);
//        Session session = new Session(sessionId, userId, token, minutes);
//        sessionRepository.save(session);
//
//        log.info(
//                "Created persistent session {} for user {}.", session.getId(), session.getUserId());
//
//        return session;
//    }
//
//    @Override
//    public Optional<Session> findSession(Long id) {
//        Objects.requireNonNull(id);
//        return sessionRepository.findById(id).map(session -> (session.isValid() ? session : null));
//    }
//
//    @Override
//    public Session getSession(Long id) throws NoSuchSessionException {
//        return findSession(id).orElseThrow(NoSuchSessionException::new);
//    }
//
//    @Override
//    public void logoutUser(Long userId) {
//        List<Long> deletedSessionIds = sessionRepository.findByUserId(userId)
//                .stream()
//                .peek(session -> session.setDeleted(true))
//                .peek(sessionRepository::save)
//                .map(Session::getId)
//                .collect(toList());
//
//        log.info("Sessions {} of user {} were deleted.", deletedSessionIds, userId);
//
//        userEventEmitter.emit(new UserEvent(userId, LOGGED_OUT));
//    }
//
//    @Override
//    public void useSession(Long id, String value, LocalDateTime lastUsedAt)
//            throws NoSuchSessionException {
//
//        Session session = getSession(id);
//        session.setToken(value);
//        session.setLastUsedAt(lastUsedAt);
//
//        sessionRepository.save(session);
//
//        log.info(
//                "Auto login with persistent session {} for user {}.", session.getId(), session.getUserId());
//
//        userEventEmitter.emit(new UserEvent(session.getUserId(), SIGNIN_REMEMBER_ME));
//    }
//
//}

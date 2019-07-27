//package com.telerik.carpooling.services.services.contracts;
//
//import com.telerik.carpooling.exceptions.NoSuchSessionException;
//import com.telerik.carpooling.models.Session;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//public interface SessionService {
//
//    Session createSession(long sessionId, long userId, String token);
//
//    Session createSession(long sessionId, long userId, String token, int minutes);
//
//    Optional<Session> findSession(Long id);
//
//    Session getSession(Long id) throws NoSuchSessionException;
//
//    void logoutUser(Long userId);
//
//    void useSession(Long id, String value, LocalDateTime lastUsedAt)
//            throws NoSuchSessionException;
//
//}
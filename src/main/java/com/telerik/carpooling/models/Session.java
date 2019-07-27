//package com.telerik.carpooling.models;
//
//import com.telerik.carpooling.security.DateTimeUtil;
//import com.telerik.carpooling.security.Entity;
//import lombok.EqualsAndHashCode;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.time.LocalDateTime;
//
//import static com.telerik.carpooling.security.DateTimeUtil.nowUtc;
//import static java.time.temporal.ChronoUnit.MINUTES;
//
//@Getter
//@Setter
//@EqualsAndHashCode
//@NoArgsConstructor
//@javax.persistence.Entity
//public class Session implements Entity<Long, Session> {
//
//    public static final int DEFAULT_EXPIRATION_MINUTES = 30 * 24 * 60;
//
//    private Long id;
//    private Long userId;
//
//    private String token;
//
//    private LocalDateTime expiresAt;
//    private LocalDateTime issuedAt;
//    private LocalDateTime lastUsedAt;
//    private LocalDateTime removedAt;
//
//    private boolean deleted;
//
//    public Session(Long id, Long userId, String token) {
//        this(id, userId, token, DEFAULT_EXPIRATION_MINUTES);
//    }
//
//    public Session(Long id, Long userId, String token, int minutes) {
//        this.id = id;
//        this.userId = userId;
//        this.token = token;
//        if (minutes == 0) {
//            minutes = DEFAULT_EXPIRATION_MINUTES;
//        }
//        expiresAt = DateTimeUtil.expireNowUtc(minutes, MINUTES);
//        issuedAt = nowUtc();
//    }
//
//    public Session(
//            Long id, Long userId, String token, LocalDateTime expiresAt, LocalDateTime issuedAt) {
//
//        this.id = id;
//        this.userId = userId;
//        this.token = token;
//        this.expiresAt = expiresAt;
//        this.issuedAt = issuedAt;
//    }
//
//    @Override
//    public Long getId() {
//        return id;
//    }
//
//    public boolean isValid() {
//        LocalDateTime now = nowUtc();
//        return isValid(now);
//    }
//
//    public boolean isValid(LocalDateTime now) {
//        return expiresAt.isAfter(now) && !deleted;
//    }
//
//    @Override
//    public boolean sameIdentityAs(Session other) {
//        return false;
//    }
//
//    public void setDeleted(boolean deleted) {
//        this.deleted = deleted;
//        this.removedAt = deleted ? nowUtc() : null;
//    }
//
//    @Override
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//}

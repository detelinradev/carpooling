//package com.telerik.carpooling.security;
//
//import com.telerik.carpooling.models.User;
//import lombok.EqualsAndHashCode;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//import java.util.UUID;
//
//import static com.telerik.carpooling.security.DateTimeUtil.expireNowUtc;
//import static java.time.temporal.ChronoUnit.MINUTES;
//
//@Getter
//@Setter
//@EqualsAndHashCode(of = "value")
//@NoArgsConstructor
//public class ConfirmationToken<P> implements Entity<Long, ConfirmationToken<P>> {
//
//    public static final int DEFAULT_EXPIRATION_MINUTES = 10;
//
//    private Long id;
//
//    private String value;
//    private User owner;
//    private ConfirmationTokenType type;
//
//    private boolean valid = true;
//    private LocalDateTime expiresAt;
//    private LocalDateTime usedAt;
//
//    private P payload;
//
//   // private AuditData<User> auditData;
//
//    public ConfirmationToken(User owner, ConfirmationTokenType type) {
//        // FIXME: Use a bit more sophisticated random token value generaton later
//        this(owner, type, DEFAULT_EXPIRATION_MINUTES);
//    }
//
//    public ConfirmationToken(User owner, ConfirmationTokenType type, int minutes) {
//        // FIXME: Use a bit more sophisticated random token value generaton later
//        this(owner, UUID.randomUUID().toString(), type, minutes);
//    }
//
//    public ConfirmationToken(User owner, String value, ConfirmationTokenType type) {
//        this(owner, value, type, DEFAULT_EXPIRATION_MINUTES, null);
//    }
//
//
//    public ConfirmationToken(User owner, String value, ConfirmationTokenType type, int minutes) {
//        this(owner, value, type, minutes, null);
//    }
//
//    public ConfirmationToken(
//            User owner, String value, ConfirmationTokenType type, int minutes, P payload) {
//
//        this.value = value;
//        this.owner = owner;
//        this.type = type;
//        this.payload = payload;
//
//        expiresAt = expireNowUtc(minutes, MINUTES);
//    }
//
//    @Override
//    public Long getId() {
//        return id;
//    }
//
//    public Optional<P> getPayload() {
//        return Optional.ofNullable(payload);
//    }
//
//    @Override
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    @Override
//    public boolean sameIdentityAs(ConfirmationToken<P> other) {
//        return equals(other);
//    }
//
//    public ConfirmationToken use() {
//        valid = false;
//        usedAt = DateTimeUtil.nowUtc();
//        return this;
//    }
//}

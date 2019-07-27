//package com.telerik.carpooling.security;
//
//import lombok.Data;
//
//import java.util.Optional;
//
//@Data
//public class UserEvent<P> implements DomainEvent<UserEvent> {
//
//    private final Long userId;
//    private final UserEventType userEventType;
//
//    private final P payload;
//
//    public UserEvent(Long userId, UserEventType userEventType) {
//        this(userId, userEventType, null);
//    }
//
//    public UserEvent(Long userId, UserEventType userEventType, P payload) {
//        this.userId = userId;
//        this.userEventType = userEventType;
//        this.payload = payload;
//    }
//
//    public Optional<P> getPayload() {
//        return Optional.ofNullable(payload);
//    }
//
//    @Override
//    public boolean sameEventAs(UserEvent other) {
//        return equals(other);
//    }
//
//}

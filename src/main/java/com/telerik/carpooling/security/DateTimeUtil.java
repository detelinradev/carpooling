//package com.telerik.carpooling.security;
//
//import java.time.LocalDateTime;
//import java.time.OffsetDateTime;
//import java.time.temporal.TemporalUnit;
//import java.util.Date;
//import java.util.Optional;
//
//import static java.time.Clock.systemUTC;
//import static java.time.ZoneOffset.UTC;
//
//public final class DateTimeUtil {
//
//    private DateTimeUtil() {
//    }
//
//    public static LocalDateTime expireNowUtc(int time, TemporalUnit unit) {
//        return nowUtc().plus(time, unit);
//    }
//
//    public static LocalDateTime nowUtc() {
//        return LocalDateTime.now(systemUTC());
//    }
//
//    public static Date toDate(LocalDateTime localDateTime) {
//        return Optional.ofNullable(localDateTime)
//                .map(ldt -> ldt.toInstant(UTC))
//                .map(Date::from)
//                .orElse(null);
//    }
//
//    public static LocalDateTime toLocalDateTime(Date date) {
//        return Optional.ofNullable(date)
//                .map(Date::toInstant)
//                .map(i -> i.atOffset(UTC))
//                .map(OffsetDateTime::toLocalDateTime)
//                .orElse(null);
//    }
//
//}
//

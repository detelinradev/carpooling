//package com.telerik.carpooling.security;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.time.Instant;
//import java.time.LocalDateTime;
//
//import static com.telerik.carpooling.security.RandomUtil.nextInt;
//import static java.time.ZoneOffset.UTC;
//import static java.time.temporal.ChronoUnit.MILLIS;
//
//public final class IdentityGenerator {
//
//
//    public static final byte DEFAULT_SHARD_ID = 0;
//
//
//    public static final Instant EPOCH = LocalDateTime.of(2017, 2, 20, 0, 0, 0, 0).toInstant(UTC);
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(IdentityGenerator.class);
//
//    private static final ThreadLocal<Serial> THREAD_LOCAL_SERIAL;
//
//    static {
//        THREAD_LOCAL_SERIAL = ThreadLocal.withInitial(() -> new Serial(nextInt(), nextInt()));
//    }
//
//    private IdentityGenerator() {
//    }
//
//
//    public static Instant extractInstant(long id) {
//        long time = (id & 0xffffffffff0000L) >>> 16;
//        return EPOCH.plusMillis(time);
//    }
//
//    public static byte extractShardId(long id) {
//        return (byte) ((id >>> 56) & 0x7fL);
//    }
//
//    public static long generate() {
//        return generate(DEFAULT_SHARD_ID);
//    }
//
//
//    public static long generate(byte shardId) {
//        long time = MILLIS.between(EPOCH, Instant.now());
//        int serial = THREAD_LOCAL_SERIAL.get().increment();
//        return doGenerate(shardId, time, serial);
//    }
//
//    static long doGenerate(byte shardId, long time, int serial) {
//        return (shardId & 0x7fL) << 56 | (time & 0xffffffffffL) << 16 | (serial & 0xffff);
//    }
//
//    static class Serial {
//
//        final int mask;
//        int value;
//
//        Serial(int value, int mask) {
//            this.value = value;
//            this.mask = mask;
//        }
//
//        int increment() {
//            return (value++ ^ mask);
//        }
//
//    }
//
//}
//

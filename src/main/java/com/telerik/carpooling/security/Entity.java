//package com.telerik.carpooling.security;
//
//public interface Entity<I, E> {
//
//    boolean sameIdentityAs(E other);
//
//    I getId();
//
//    default void setId(I id) {
//    }
//
//    default boolean isNew() {
//        if (getId() == null) {
//            return true;
//        }
//        return false;
//    }
//
//}

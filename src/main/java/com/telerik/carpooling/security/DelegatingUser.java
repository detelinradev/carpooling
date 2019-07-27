//package com.telerik.carpooling.security;
//
//import com.telerik.carpooling.models.User;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//
//public class DelegatingUser implements UserDetails {
//
//    private final User user;
//
//    public DelegatingUser(User user) {
//        this.user = user;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return null;
//    }
//
//    @Override
//    public String getPassword() {
//        return null;
//    }
//
//    @Override
//    public String getUsername() {
//        return String.valueOf(user.getId());
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return !user.isDeleted();
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
////                !user.isLocked();
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
////                user.isConfirmed();
//    }
//
//}

package com.rag.chatbot.Config;

import com.rag.chatbot.Entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // You can define roles and authorities here. For now, it's empty.
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // Assuming email is the username.
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Customize if necessary.
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Customize if necessary.
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Customize if necessary.
    }

    @Override
    public boolean isEnabled() {
        return true; // Customize if necessary.
    }

    public User getUser() {
        return user;
    }
}

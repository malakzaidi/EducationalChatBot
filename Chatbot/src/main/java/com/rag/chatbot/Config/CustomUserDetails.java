package com.rag.chatbot.Config;

import com.rag.chatbot.Entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final User user;

    // Constructor to initialize the User entity
    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Return authorities/roles here. For now, it's empty as there are no roles defined.
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        // Returning the password from the User entity
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        // Returning the email as the username
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        // Account is considered non-expired for now (can be customized if needed)
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // Account is considered non-locked (customize this logic if necessary)
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // Credentials are considered non-expired (can be customized if necessary)
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Account is considered enabled (you can customize this logic)
        return true;
    }

    // Accessor method to get the User entity (if needed elsewhere)
    public User getUser() {
        return user;
    }
}

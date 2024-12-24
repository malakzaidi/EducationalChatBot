package com.rag.chatbot.Entity;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.Set;

public enum Role {
    USER,
    ADMIN;

    public Set<SimpleGrantedAuthority> getAuthorities() {
        switch (this) {
            case ADMIN:
                return Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"));
            case USER:
            default:
                return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }
}

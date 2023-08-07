package com.food.delivery.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class SecurityUser extends User {
    private final Long userId;
    private final String key;
    public SecurityUser(Long userId, String key, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.userId = userId;
        this.key = key;
    }

    public Long getUserId() {
        return userId;
    }

    public String getKey() {
        return key;
    }
}

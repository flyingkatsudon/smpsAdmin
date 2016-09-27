package com.humane.smps.service;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUserDetails extends User {

    @Getter
    private String userAdmissions;

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, String userAdmissions) {
        super(username, password, authorities);
        this.userAdmissions = userAdmissions;
    }
}

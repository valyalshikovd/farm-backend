package com.example.farmbackend.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Objects;

public class RolesCheck {

    public static boolean authoritiesCheck(Authentication authentication, String Role){
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            if(Objects.equals(authority.getAuthority(), Role)){
                return true;
            }
        }
        return false;
    }
}

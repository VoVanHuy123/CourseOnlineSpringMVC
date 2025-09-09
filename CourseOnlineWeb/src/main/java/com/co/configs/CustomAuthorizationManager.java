/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.configs;

import java.util.function.Supplier;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

/**
 *
 * @author ACER
 */
public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext>{
    private final String requiredRole;
    private final boolean mustBeVerified;

    public CustomAuthorizationManager(String requiredRole, boolean mustBeVerified) {
        this.requiredRole = requiredRole;
        this.mustBeVerified = mustBeVerified;
    }
    
    public static CustomAuthorizationManager verifiedTeacher() {
    return new CustomAuthorizationManager("teacher", true);
    }

    public static CustomAuthorizationManager verifiedStudent() {
        return new CustomAuthorizationManager("student", true);
    }

    public static CustomAuthorizationManager adminOnly() {
        return new CustomAuthorizationManager("admin", false);
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        Authentication auth = authentication.get();
        if (auth != null && auth.getPrincipal() instanceof CustomUserDetails user) {
            boolean hasRole = requiredRole.equalsIgnoreCase(user.getRole());
            boolean isVerified = !mustBeVerified || Boolean.TRUE.equals(user.isVerify());
            return new AuthorizationDecision(hasRole && isVerified);
        }
        return new AuthorizationDecision(false);
    }
}

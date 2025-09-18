package com.co.configs;

import com.co.dtos.CourseDTO;
import com.co.services.CourseServices;
import java.util.function.Function;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;


public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final String requiredRole;
    private final boolean mustBeVerified;
    private final Function<RequestAuthorizationContext, Integer> ownerIdExtractor;

    @Autowired
    private CourseServices courseServices; // Spring tự inject

    // Constructor private để chỉ cho phép tạo qua factory method
    private CustomAuthorizationManager(String requiredRole, boolean mustBeVerified,
                                       Function<RequestAuthorizationContext, Integer> ownerIdExtractor) {
        this.requiredRole = requiredRole;
        this.mustBeVerified = mustBeVerified;
        this.ownerIdExtractor = ownerIdExtractor;
    }

    // ===== Factory methods =====
    public static CustomAuthorizationManager verifiedTeacher() {
        return new CustomAuthorizationManager("teacher", true, null);
    }

    public static CustomAuthorizationManager verifiedTeacherAndOwner() {
        return new CustomAuthorizationManager("teacher", true, ctx -> {
            String idStr = ctx.getVariables().get("id");
            return idStr != null ? Integer.valueOf(idStr) : null;
        });
    }

    public static CustomAuthorizationManager adminOnly() {
        return new CustomAuthorizationManager("admin", false, null);
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        Authentication auth = authentication.get();
        if (auth != null && auth.getPrincipal() instanceof CustomUserDetails user) {
            boolean hasRole = requiredRole.equalsIgnoreCase(user.getRole());
            boolean isVerified = !mustBeVerified || Boolean.TRUE.equals(user.isVerify());

            boolean isOwner = true;
            if (ownerIdExtractor != null) {
                Integer courseId = ownerIdExtractor.apply(context);
                if (courseId != null) {
                    CourseDTO courseDTO = courseServices.getCourseById(courseId, false);
                    isOwner = courseDTO != null && courseDTO.getTeacherId() == user.getId();
                } else {
                    isOwner = false;
                }
            }

            return new AuthorizationDecision(hasRole && isVerified && isOwner);
        }
        return new AuthorizationDecision(false);
    }
}


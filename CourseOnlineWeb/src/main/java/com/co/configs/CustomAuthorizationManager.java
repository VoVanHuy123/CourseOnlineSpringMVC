package com.co.configs;

import com.co.dtos.CourseDTO;
import com.co.dtos.ReviewDTO;
import com.co.services.CourseServices;
import com.co.services.ReviewServices;
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
    private final String type;

    @Autowired
    private CourseServices courseServices; 
    @Autowired
    private ReviewServices reviewServices; 

    private CustomAuthorizationManager(String requiredRole, boolean mustBeVerified,
            Function<RequestAuthorizationContext, Integer> ownerIdExtractor, String type) {
        this.requiredRole = requiredRole;
        this.mustBeVerified = mustBeVerified;
        this.ownerIdExtractor = ownerIdExtractor;
        this.type = type;
    }

    
    public static CustomAuthorizationManager verifiedTeacher() {
        return new CustomAuthorizationManager("teacher", true, null, null);
    }

    public static CustomAuthorizationManager verifiedTeacherAndOwner() {
        return new CustomAuthorizationManager("teacher", true, ctx -> {
            String idStr = ctx.getVariables().get("id");
            return idStr != null ? Integer.valueOf(idStr) : null;
        }, "course");
    }
    public static CustomAuthorizationManager verifiedStudentAndReviewOwner() {
        return new CustomAuthorizationManager("student", true, ctx -> {
            String idStr = ctx.getVariables().get("id");
            return idStr != null ? Integer.valueOf(idStr) : null;
        }, "review");
    }

    public static CustomAuthorizationManager adminOnly() {
        return new CustomAuthorizationManager("admin", false, null, null);
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        Authentication auth = authentication.get();
        if (auth != null && auth.getPrincipal() instanceof CustomUserDetails user) {
            boolean hasRole = requiredRole.equalsIgnoreCase(user.getRole());
            boolean isVerified = !mustBeVerified || Boolean.TRUE.equals(user.isVerify());

            boolean isOwner = true;
            if (ownerIdExtractor != null) {
                switch (type) {
                    case "course":
                        Integer courseId = ownerIdExtractor.apply(context);
                        if (courseId != null) {
                            CourseDTO courseDTO = courseServices.getCourseById(courseId, false);
                            isOwner = courseDTO != null && courseDTO.getTeacherId() == user.getId();
                        } else {
                            isOwner = false;
                        }
                        break;
                    case "review":
                        Integer reviewId = ownerIdExtractor.apply(context);
                        if (reviewId != null) {
                            ReviewDTO review = this.reviewServices.getReviewById(reviewId);
                            System.out.println(review);
                            isOwner = review != null && review.getUserId()== user.getId();
                        } else {
                            isOwner = false;
                        }
                        break;
                    default:
                        
                }

            }

            return new AuthorizationDecision(hasRole && isVerified && isOwner);
        }
        return new AuthorizationDecision(false);
    }
}

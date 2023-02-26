package com.hike.models;

import com.hike.security.CustomOAuth2User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;

public class Utility {
    public static String getSiteURL(HttpServletRequest request){
        String siteURL= request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    public static String getLoggedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomOAuth2User) {
            CustomOAuth2User oauth2User = (CustomOAuth2User) principal;
            return oauth2User.getAttribute("name");
        } else if (principal instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) principal;
            return userDetails.getUsername();
        }
        return null;
    }
}

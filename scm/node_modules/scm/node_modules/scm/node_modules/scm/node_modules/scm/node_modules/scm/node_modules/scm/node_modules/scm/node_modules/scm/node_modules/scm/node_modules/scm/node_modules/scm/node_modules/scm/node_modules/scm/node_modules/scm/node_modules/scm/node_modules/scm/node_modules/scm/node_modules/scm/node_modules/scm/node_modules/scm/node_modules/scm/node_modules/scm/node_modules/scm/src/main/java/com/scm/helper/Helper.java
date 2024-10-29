package com.scm.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Component;

@Component
public class Helper {

    @Value("${server.baseUrl}")
    private String baseUrl;

    public static String getLoggedInUserEmail(Authentication authentication) {

        // first find out that use is logged with normal or gogle or gitbub

        String username = "";
        if (authentication instanceof OAuth2AuthenticationToken) {
            var oauth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
            String authorizedClientRegistrationId = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();
            DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();

            if (authorizedClientRegistrationId.equalsIgnoreCase("google")) {
                System.out.println("fetching username form google");
                username = user.getAttribute("email");
            } else if (authorizedClientRegistrationId.equalsIgnoreCase("Github")) {
                System.out.println("fetching username form github");
                username = user.getAttribute("email");
            }

            return username;

        } else {
            // Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();

            // if (authentication1 != null && authentication1.isAuthenticated()) {
            //     Object principal = authentication.getPrincipal();

            //     // Log or debug the Authentication object
            //     System.out.println("Authentication Object: " + authentication1.toString());

            //     if (principal instanceof UserDetails) {
            //         username = ((UserDetails) principal).getUsername(); // This should return the email
            //     } else {
            //         username = authentication1.getName(); // Fallback, this should return the email
            //     }
            // }

            username = authentication.getName();

        }
        return username;

    }


    public String getLinkForEmailVerification(String emailToken){
        String Link = this.baseUrl+"/scm/auth/verify-email?token=" + emailToken;

        
    
     
        return Link;
    }
}

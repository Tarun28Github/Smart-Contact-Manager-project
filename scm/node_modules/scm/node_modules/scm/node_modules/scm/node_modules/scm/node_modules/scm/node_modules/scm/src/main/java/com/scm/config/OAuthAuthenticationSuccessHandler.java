package com.scm.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.scm.entities.Provider;
import com.scm.entities.User;
import com.scm.helper.AppConstants;
import com.scm.repositories.UserRepo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    @Autowired
    private UserRepo userRepo;

    Logger logger = LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);



    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        
                logger.info("OAuthAuthenticationSuccessHandler");

                  //identify the user provider
                  var oauth2AuthenticationToken= (OAuth2AuthenticationToken)authentication;

                  String authorizedClientRegistrationId = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();
                
                  logger.info(authorizedClientRegistrationId);
                  DefaultOAuth2User user=(DefaultOAuth2User)authentication.getPrincipal();
                  user.getAttributes().forEach( (key, value) -> 
                 logger.info("{} => {}", key,value  )
                 );

                 User user1 = new User();
                 user1.setEnabled(true);
                 user1.setId(UUID.randomUUID().toString());
                 user1.setRoleslist(List.of(AppConstants.ROLE_USER));
                 user1.setEmailVerified(true);
                 
                
                  if(authorizedClientRegistrationId.equalsIgnoreCase("google")){

                    //google login
                    user1.setEmail(user.getAttribute("email"));
                    user1.setName(user.getAttribute("name"));
                    user1.setPassword("password");
                    user1.setProfilepic(user.getAttribute("picture"));
                    user1.setAbout("User loging with google");
                    user1.setProvider(Provider.GOOGLE);
                    user1.setProviderUserId(user.getName());



                  }
                  else if(authorizedClientRegistrationId.equalsIgnoreCase("Github")){

                                        //Github login
                                        user1.setEmail(user.getAttribute("email"));
                                        user1.setName(user.getAttribute("login"));
                                        user1.setPassword("password");
                                        user1.setProfilepic(user.getAttribute("avatar_url"));
                                        user1.setAbout("User login with Github");
                                        user1.setProvider(Provider.GITHUB);
                                        user1.setProviderUserId(user.getName());
                    

                  }
                  else{
                    logger.info("normal user");
                  }


                // get and save the logged user in our database;

                // DefaultOAuth2User user=(DefaultOAuth2User)authentication.getPrincipal();

                // logger.info(user.getName());
                
                // user.getAttributes().forEach( (key, value) -> 
                //  logger.info("{} => {}", key,value  )
                // );

                // logger.info(user.getAuthorities().toString());

             
               
               
               
                // // saveing user in our database;

                // String email = user.getAttribute("email");
                // String name = user.getAttribute("name");
                // String picture = user.getAttribute("picture");

                // User user1 = new User();
                // 
                // 
                // 
                // 
                // 
                //
                // 
                //
                // 
                // 
                

                User user2 =userRepo.findByEmail(user1.getEmail()).orElse(null);

                if(user2==null){
                    userRepo.save(user1);
                }

                new DefaultRedirectStrategy().sendRedirect(request, response, "/scm/user/profile");

    }



}

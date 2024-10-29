package com.scm.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.scm.entities.User;
import com.scm.helper.Helper;
import com.scm.services.UserService;

@ControllerAdvice
public class RootController {

     @Autowired
    UserService userService;
    private Logger logger = LoggerFactory.getLogger(UserController.class);

    // getting logged in user

    @ModelAttribute
    public void getLoggedInUser(Model model, Authentication authentication) {

        if (authentication==null){
            return;
        }

        System.out.println("getting logged in user ");

        String name = Helper.getLoggedInUserEmail(authentication);

        logger.info("user logged in : {}", name);

        // fetching user data using username;

        User user = userService.getUserByEmail(name);

        System.out.println(user.getName());
        System.out.println(user.getEmail());

        model.addAttribute("loggedinUser", user);

    }

}

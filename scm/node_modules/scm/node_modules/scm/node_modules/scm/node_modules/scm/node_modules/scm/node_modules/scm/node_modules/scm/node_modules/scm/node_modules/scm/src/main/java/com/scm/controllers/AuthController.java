package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.User;
import com.scm.helper.Message;
import com.scm.helper.MessageType;
import com.scm.repositories.UserRepo;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/scm/auth")
public class AuthController {

    @Autowired
    private UserRepo repo;

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("token") String token, HttpSession session){

        User user= repo.findByEmailToken(token).orElse(null);


        if(user != null){

            if(user.getEmailToken().equals(token)  ){
                user.setEmailVerified(true);
                user.setEnabled(true);
                repo.save(user);

                session.setAttribute("message", Message.builder().type(MessageType.green).message("Your Email is verified. Now you can login.").build());

                return "success_page";
            }


           
        }
        session.setAttribute("message", Message.builder().type(MessageType.red).message("Email is not verified ! token is not associated with user.").build());
        return "error_page";
    }
}

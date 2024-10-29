package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.services.UserService;

@Controller
@RequestMapping("/scm/user")
public class UserController {

    @Autowired
    UserService userService;

    

    // user dashboard page

    @GetMapping("/dashboard")
    public String userDashboard(Model model) {
        System.out.println("User dashboard");
        return "user/dashboard";
    }

    // user profile page

    @GetMapping("/profile")
    public String userProfile(Model model, Authentication authentication) {
        System.out.println("user profile");
        // getting logedin user username

        return "user/profile";
    }

    // add contact
    @GetMapping("/add")
    public String addContact() {
        System.out.println("add contact page");
        return "user/add_contact";
    }

    // view contact

    @GetMapping("/view")
    public String viewContact() {
        System.out.println("view contact page");
        return "user/view_contact";
    }

    // delete contact
    @GetMapping("/delete")
    public String deleteContact() {
        System.out.println("delete contact");
        return "user/delete_contact";
    }

    // edit contact
    @GetMapping("/edit")
    public String editContact() {
        System.out.println("edit contact");
        return "user/edit_contact";
    }

}

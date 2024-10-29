package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.entities.User;
import com.scm.form.UserForm;
import com.scm.helper.Message;
import com.scm.helper.MessageType;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@RequestMapping("/scm")
@Controller
public class PageController {

	@Autowired
	private UserService userService;

	@GetMapping("/home")
	public String home(Model m) {

		m.addAttribute("name","tomar");
		m.addAttribute("channel","durgeshtiwari");
		System.out.println("home page running");
		return "home";
	}

	
	@GetMapping("/about")
	public String about(){
		System.out.println("about page running");
		return "about";
	}
	@GetMapping("/service")
	public String service(){
		System.out.println("services page running");
		return "service";
	}

	@GetMapping("/contact")
	public String contact(){
		System.out.println("contact page running");
		return "contact";
	}

	@GetMapping("/register")
	public String register(Model m){
		System.out.println("register page running");
		UserForm userForm = new UserForm();
		m.addAttribute("userForm", userForm);
	    return "register";
	}

	@GetMapping("/login")
	public String login(){
		System.out.println("login page running");
	    return "login";
	}

	@PostMapping("/process_form")
	public String processform(@Valid @ModelAttribute UserForm userForm ,BindingResult rBindingResult,HttpSession session){
		System.out.println("processing User form");
		System.out.println(userForm);

		//validate form data
		if (rBindingResult.hasErrors()) {
			return "register";
		}

		// saving user
        //UserForm -> user
		// User user= User.builder()
		// .name(userForm.getName())
		// .email(userForm.getEmail())
		// .password(userForm.getPassword())
		// .phonenumber(userForm.getPhonenumber())
		// .about(userForm.getAbout())
		// .build();

		User user = new User();
		user.setName(userForm.getName());
		user.setEmail(userForm.getEmail());
		user.setPassword(userForm.getPassword());
		user.setPhonenumber(userForm.getPhonenumber());
		user.setAbout(userForm.getAbout());
		user.setEnabled(false);
		
		User savUser = userService.saveUser(user);
		System.out.println(savUser);

		System.out.println("User saved !!!!!!!!");

		Message message = Message.builder().message("User Registration success full").type(MessageType.green).build();


		session.setAttribute("message", message);


		return "redirect:register";
	}
}

package com.scm.controllers;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.form.ContactForm;
import com.scm.form.SearchContactForm;
import com.scm.helper.AppConstants;
import com.scm.helper.Helper;
import com.scm.helper.Message;
import com.scm.helper.MessageType;
import com.scm.services.ContactService;
import com.scm.services.ImageService;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;





@Controller
@RequestMapping("/scm/user/contacts")
public class ContactController {

    Logger logger = LoggerFactory.getLogger(ContactController.class);

    @Autowired
    private ContactService contactService;
    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;
    
    @GetMapping("/add_contact")
    public String addContacts(Model model){

        System.out.println("addcontact page");

        ContactForm contactForm= new ContactForm();
        model.addAttribute("contactForm", contactForm);
        return "user/addcontact";
    }

    @PostMapping("/processcontactform")
    public String processContactForm(@Valid @ModelAttribute ContactForm contactForm,BindingResult rBindingResult, HttpSession session,Authentication authentication) {
        
        System.out.println("prcessing contact form");
        System.out.println(contactForm);

        //validating form

        if(rBindingResult.hasErrors()){
            //sending message on error
            rBindingResult.getAllErrors().forEach(error -> logger.info(error.toString()));

            Message message = Message.builder().message("Please Resolve All Error").type(MessageType.red).build();
		    session.setAttribute("message", message);
            return "user/addcontact";
        }

         // processing contact image

          logger.info(contactForm.getContactImage().getOriginalFilename());

          //upload image on cloudinary
          String filename = UUID.randomUUID().toString();
          String ImgUrl= imageService.uploadImage(contactForm.getContactImage(),filename);




        // saving contact details in users contact

        
        Contact contact = new Contact();
        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setPhonenumber(contactForm.getPhonenumber());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDiscription());
        contact.setWebsitelink(contactForm.getWebsitelink());
        contact.setLinkedlink(contactForm.getLinkedlink());
        contact.setFavorite(contactForm.isFavorite());
        contact.setPicture(ImgUrl);
        contact.setCloudinaryImagePublicId(filename);
    

        //getting logged in user detail

        String name = Helper.getLoggedInUserEmail(authentication);
        User user = userService.getUserByEmail(name);

        //setting user
        contact.setUser(user);

        //saving contact

        Contact contact2 =  contactService.saveContact(contact);

        System.out.println(contact2);

        

        System.out.println("Contact Saved !!!!!!!");

        //sending  message on saving
        Message message = Message.builder().message("Contact success fully added").type(MessageType.green).build();
		session.setAttribute("message", message);
        return "redirect:/scm/user/contacts/add_contact";
    }
    

    //view contact
    @GetMapping("/view_contact")
    public String viewContact(
        @ModelAttribute SearchContactForm searchContactForm,
    @RequestParam(value = "page",defaultValue = "0") int page,
    @RequestParam(value = "size", defaultValue = "10") int size,
    @RequestParam(value = "sortBy",defaultValue = "name") String sortBy,
    @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection   
    ,Model model,Authentication authentication) {

        String username= Helper.getLoggedInUserEmail(authentication);
        
        User user = userService.getUserByEmail(username);

        Page<Contact> pageContactList=contactService.getContactByUser(user,page,size,sortBy,sortDirection);

        model.addAttribute("pageContactList", pageContactList);
        model.addAttribute("size",AppConstants.PAGE_SIZE);
        
        return "user/viewcontact";
    }

    @GetMapping("/search")
    public String searchContact(
        @ModelAttribute SearchContactForm searchContactForm,
        @RequestParam(value = "page",defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size,
        @RequestParam(value = "sortBy",defaultValue = "name") String sortBy,
        @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection   
        ,Model model,Authentication authentication
        ){

            var user = userService.getUserByEmail(Helper.getLoggedInUserEmail(authentication));

            Page<Contact>  pageContact=null;

            if(searchContactForm.getField().equalsIgnoreCase("name")){
                pageContact = contactService.searchByName(user, searchContactForm.getKeyword(), size, page, sortBy, sortDirection);
            }
            else if(searchContactForm.getField().equalsIgnoreCase("email")){
                pageContact = contactService.searchByEmail(user, searchContactForm.getKeyword(), size, page, sortBy, sortDirection);
            }
            else if(searchContactForm.getField().equalsIgnoreCase("phone")){
                pageContact= contactService.searchByPhone(user, searchContactForm.getKeyword(), size, page, sortBy, sortDirection);
            }

            model.addAttribute("searchPageContact", pageContact);
            model.addAttribute("size",AppConstants.PAGE_SIZE);



        return "user/search";
    } 

    @GetMapping("/delete/{id}")
    public String deleteContact(@PathVariable String id, HttpSession session){
        contactService.deleteContact(id);

      Message message =   Message.builder().type(MessageType.green).message("Successfully deleted").build();


        session.setAttribute("message", message);
        return "redirect:/scm/user/contacts/view_contact";
    } 

    @GetMapping("/view/{id}")
    public String updateContactFormView(@PathVariable String id,Model model) {
        
        var contact=contactService.getContactById(id);

        ContactForm contactForm = new ContactForm();
        contactForm.setName(contact.getName());
        contactForm.setEmail(contact.getEmail());
        contactForm.setAddress(contact.getAddress());
        contactForm.setDiscription(contact.getDescription());
        contactForm.setPhonenumber(contact.getPhonenumber());
        contactForm.setWebsitelink(contact.getWebsitelink());
        contactForm.setLinkedlink(contact.getLinkedlink());
        contactForm.setFavorite(contact.isFavorite());
        contactForm.setPicture(contact.getPicture());
        
        model.addAttribute("contactForm", contactForm);
        model.addAttribute("contactid", id);
        return "user/update_contact_view";
    }

    @PostMapping("/update/{id}")
    public String updateContact(@PathVariable String id ,@Valid @ModelAttribute ContactForm contactForm,  BindingResult rBindingResult,Model model,HttpSession session) {
      
         //validating form

         if(rBindingResult.hasErrors()){
            //sending message on error
            rBindingResult.getAllErrors().forEach(error -> logger.info(error.toString()));

            Message message = Message.builder().message("Please Resolve All Error").type(MessageType.red).build();
		    session.setAttribute("message", message);
            return "user/update_contact_view";
        }

        //updateing contact


        Contact contact = contactService.getContactById(id);
        contact.setId(id);
        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDiscription());
        contact.setPhonenumber(contactForm.getPhonenumber());
        contact.setWebsitelink(contactForm.getWebsitelink());
        contact.setLinkedlink(contactForm.getLinkedlink());
        contact.setFavorite(contactForm.isFavorite());
        
        
        //processimage

        if(contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()){
            logger.info("file is not empty");
            String filename = UUID.randomUUID().toString();
            String ImgUrl= imageService.uploadImage(contactForm.getContactImage(),filename);
            contact.setCloudinaryImagePublicId(filename);
            contact.setPicture(ImgUrl);
        }
        else{
            logger.info("file is empty");
        }

        var updatedContact=contactService.updateContact(contact);
        session.setAttribute("message", Message.builder().type(MessageType.green).message("contact updated successfully").build());
        
        return "redirect:/scm/user/contacts/view/"+id;
    }

    @GetMapping("/view/reset/{id}")
    public String reset(@PathVariable String id, Model model) {

        ContactForm contactForm = new ContactForm();
        model.addAttribute("id", id);
        
        model.addAttribute("contactForm", contactForm);


        return "user/update_contact_view";
    }
    
    
    
}

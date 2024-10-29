package com.scm.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.helper.ResourceNotFoundException;
import com.scm.repositories.ContactRepo;
import com.scm.services.ContactService;

@Service
public class ContactServiceImpl implements ContactService{


    @Autowired
    private ContactRepo contactRepo;


    @Override
    public Contact saveContact(Contact contact) {
        String contactId = UUID.randomUUID().toString();

        contact.setId(contactId);
        
        return contactRepo.save(contact);
    }

    @Override
    public Contact getContactByName(String name) {
        return contactRepo.findByName(name).orElseThrow(()-> new ResourceNotFoundException("Contact Not Found"));
    }

    @Override
    public Contact getContactByEmail(String email) {
        return contactRepo.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("Contact Not Found"));
    }

    @Override
    public Contact updateContact(Contact contact) {
        // Contact contact1 = contactRepo.findById(contact.getId()).orElseThrow(()-> new ResourceNotFoundException("Contact not found"));
        // contact1.setName(contact.getName());
        // contact1.setEmail(contact.getEmail());
        // contact1.setAddress(contact.getAddress());
        // contact1.setDescription(contact.getDescription());
        // contact1.setPhonenumber(contact.getPhonenumber());
        // contact1.setLinkedlink(contact.getLinkedlink());
        // contact1.setWebsitelink(contact.getWebsitelink());
        // contact1.setPicture(contact.getPicture());
        // contact1.setFavorite(contact.isFavorite());
        // contact1.setCloudinaryImagePublicId(contact.getCloudinaryImagePublicId());
        
        

        return contactRepo.save(contact);
    }

    @Override
    public List<Contact> getAllContacts() {
        return contactRepo.findAll();
    }

    @Override
    public void deleteContact(String id) {
        contactRepo.deleteById(id);
    }

    @Override
    public Contact getContactById(String id) {
        return contactRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Contact Not Found"));
    }

    @Override
    public List<Contact> getContactByUserid(String id) {
        return contactRepo.findByUserId(id);
    }

    @Override
    public Page<Contact> getContactByUser(User user , int page, int size , String sortBy , String direction) {
      
        Sort sort =   direction.equals("desc")? Sort.by(sortBy).descending(): Sort.by(sortBy).ascending();
        
        PageRequest pageable = PageRequest.of(page, size, sort);
      
        return contactRepo.findByUser(user, pageable);
    }

    @Override
    public Page<Contact> searchByName(User user ,String namekeyword, int size, int page, String sortBy, String direction) {
        
        Sort sort =   direction.equals("desc")? Sort.by(sortBy).descending(): Sort.by(sortBy).ascending();
        
        PageRequest pageable = PageRequest.of(page, size, sort);
        return contactRepo.findByUserAndNameContaining(user, namekeyword, pageable);
    }

    @Override
    public Page<Contact> searchByPhone(User user ,String phonekeyword, int size, int page, String sortBy, String direction) {
        Sort sort =   direction.equals("desc")? Sort.by(sortBy).descending(): Sort.by(sortBy).ascending();
        
        PageRequest pageable = PageRequest.of(page, size, sort);
        return contactRepo.findByUserAndPhonenumberContaining(user,phonekeyword, pageable);
    }

    @Override
    public Page<Contact> searchByEmail(User user ,String emailkeyword, int size, int page, String sortBy, String direction) {
        Sort sort =   direction.equals("desc")? Sort.by(sortBy).descending(): Sort.by(sortBy).ascending();
        
        PageRequest pageable = PageRequest.of(page, size, sort);

        return contactRepo.findByUserAndEmailContaining(user,emailkeyword, pageable);
    }

    

}

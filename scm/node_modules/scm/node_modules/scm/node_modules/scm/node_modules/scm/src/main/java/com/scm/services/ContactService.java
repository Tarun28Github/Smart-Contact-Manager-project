package com.scm.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.scm.entities.Contact;
import com.scm.entities.User;

public interface ContactService {

    Contact saveContact(Contact contact);
    Contact getContactById(String id);
    Contact getContactByName(String name);
    Contact getContactByEmail(String email);
    Contact updateContact(Contact contact);
    List<Contact> getAllContacts();
    void deleteContact(String id);
    List<Contact> getContactByUserid(String id);
    Page<Contact> getContactByUser(User user , int page, int size ,String sortBy , String direction);

    Page<Contact> searchByName(User user ,String namekeyword, int size, int page, String sortBy, String direction);
    Page<Contact> searchByPhone(User user ,String phonekeyword, int size, int page, String sortBy, String direction);
    Page<Contact> searchByEmail(User user ,String emailkeyword, int size, int page, String sortBy, String direction);


}

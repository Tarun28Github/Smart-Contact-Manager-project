package com.scm.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.scm.entities.User;
import com.scm.helper.AppConstants;
import com.scm.helper.Helper;
import com.scm.helper.ResourceNotFoundException;
import com.scm.repositories.UserRepo;
import com.scm.services.EmailService;
import com.scm.services.UserService;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EmailService service;

    @Autowired
    private Helper helper;
@Autowired
private PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) {

        String userid=UUID.randomUUID().toString();
        user.setId(userid);

        // set encoded password

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        //set user roles
        user.setRoleslist(List.of(AppConstants.ROLE_USER));


        
        String emailToken = UUID.randomUUID().toString();

        String tokenLink = helper.getLinkForEmailVerification(emailToken);

        user.setEmailToken(emailToken);
        User saveduser = userRepo.save(user);

        service.sendEmail( saveduser.getEmail(), "Verify your Email : Smart Contact Manager", "Please Click on link to verify "+ tokenLink );

        return saveduser;
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepo.findById(id);
    }

    @Override
    public Optional<User> updateUser(User user) {
        User user2=userRepo.findById(user.getId()).orElseThrow(()-> new ResourceNotFoundException("User not found"));   

        
        user2.setName(user.getName());
        user2.setEmail(user.getEmail());
        user2.setPassword(user.getPassword());
        user2.setAbout(user.getAbout());
        user2.setPhonenumber(user.getPhonenumber());
        user2.setPhoneVerified(user.isPhoneVerified());
        user2.setEmailVerified(user.isEmailVerified());
        user2.setEnabled(user.isEnabled());
        user2.setProfilepic(user.getProfilepic());
        user2.setProvider(user.getProvider());
        user2.setProviderUserId(user.getProviderUserId());

        User save = userRepo.save(user2);

        return Optional.ofNullable(save);

    }
    @Override
    public void deleteUser(String id) {
        User user1=userRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("User not found"));   
        

        userRepo.delete(user1);
    }

    @Override
    public Boolean isUserExist(String id) {
        return userRepo.findById(id).isPresent();
    }

    @Override
    public Boolean isUserExistByEmail(String email) {
        return userRepo.findByEmail(email).isPresent();
    }

    @Override
    public List<User> getAllUsers() {
       return userRepo.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("User not found") );
    }

}

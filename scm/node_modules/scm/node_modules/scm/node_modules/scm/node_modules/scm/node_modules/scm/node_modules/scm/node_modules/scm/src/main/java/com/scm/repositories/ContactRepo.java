package com.scm.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.scm.entities.Contact;
import com.scm.entities.User;


@Repository
public interface ContactRepo  extends JpaRepository<Contact, String> {

    Optional<Contact>  findByName(String name);
    Optional<Contact>  findByEmail(String email);

    Page<Contact>  findByUser(User user, Pageable pageable);

    @Query("SELECT c FROM Contact c WHERE c.user.id = :userId")
    List<Contact>  findByUserId(@Param("userId") String userId);

    Page<Contact>  findByUserAndNameContaining(User user,String name, Pageable pageable);
    Page<Contact>  findByUserAndEmailContaining(User user, String email, Pageable pageable);
    Page<Contact>  findByUserAndPhonenumberContaining(User user, String phone, Pageable pageable);

}

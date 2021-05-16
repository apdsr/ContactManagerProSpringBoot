package com.contactmanager.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.contactmanager.entities.Contact;
import com.contactmanager.entities.User;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

	// pagination
	
	@Query("from Contact as c where c.user.userId=:userId")
	public Page<Contact> findContactByUser(@Param("userId") int userId, Pageable pageable);
	
	
	public List<Contact> findByContactNameContainingAndUser(String keywords,User user);
	
   public List<Contact> findBycontactEmailContainingAndUser(String email,User user);
	
	
}


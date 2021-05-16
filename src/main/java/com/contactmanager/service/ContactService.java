package com.contactmanager.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.contactmanager.entities.Contact;
import com.contactmanager.entities.User;

public interface ContactService {

	public Page<Contact> fetchContactsByUser(int userId,int page);
	
	public Optional<Contact> fetchByID(int cid);
	
	public void deleteContact(Contact contact);
	
	public void saveUpdatedContact(Contact contact);
	
	public List<Contact> findByNameAndUser(String keywords,User user);
	
	public List<Contact> findByEmailAndUser(String email,User user);
}

package com.contactmanager.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.contactmanager.entities.Contact;
import com.contactmanager.entities.User;
import com.contactmanager.repository.ContactRepository;

@Service
public class ContactSeviceImpl implements ContactService {

	@Autowired
	private ContactRepository contactRepository;

	@Override
	public Page<Contact> fetchContactsByUser(int userId, int page) {

		Pageable pageable = PageRequest.of(page, 4);
		Page<Contact> contacts = this.contactRepository.findContactByUser(userId, pageable);

		return contacts;
	}

	@Override
	public Optional<Contact> fetchByID(int cid) {
		Optional<Contact> contactOPt = this.contactRepository.findById(cid);
		return contactOPt;
	}

	@Override
	public void deleteContact(Contact contact) {
		this.contactRepository.delete(contact);

	}

	@Override
	public void saveUpdatedContact(Contact contact) {
		this.contactRepository.save(contact);

	}

	@Override
	public List<Contact> findByNameAndUser(String keywords, User user) {
		return this.contactRepository.findByContactNameContainingAndUser(keywords, user);
	}

	@Override
	public List<Contact> findByEmailAndUser(String email, User user) {
		
		return this.contactRepository.findBycontactEmailContainingAndUser(email, user);
	}

}

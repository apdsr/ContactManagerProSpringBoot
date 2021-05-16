package com.contactmanager.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.contactmanager.entities.Contact;
import com.contactmanager.entities.User;
import com.contactmanager.service.ContactService;
import com.contactmanager.service.UserService;

@RestController
public class SearchController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private ContactService contactService;
	
	
	//search handler
	
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String query,Principal principal){
	
		System.out.println(query);
		
	 	User user= this.userService.fetchUserByUserName(principal.getName());
		
		List<Contact> contacts = this.contactService.findByNameAndUser(query, user);
		
		return ResponseEntity.ok(contacts);
		
	}

}

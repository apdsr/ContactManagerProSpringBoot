package com.contactmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.contactmanager.entities.Contact;
import com.contactmanager.entities.User;
import com.contactmanager.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public User saveUserData(User user) {
		User res = this.userRepository.save(user);
		return res;
	}

	@Override
	public User fetchUserByUserName(String username) {
		
		return  this.userRepository.getUserByUserName(username);
	}

	@Override
	public void deleteUser(User user) {
		 
		this.userRepository.delete(user);
		
	}



}

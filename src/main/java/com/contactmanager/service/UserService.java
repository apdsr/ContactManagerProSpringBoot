package com.contactmanager.service;

import com.contactmanager.entities.User;

public interface UserService {
  
	public User saveUserData(User user);
	
	public User fetchUserByUserName(String username);
	
	public void deleteUser(User user);
}

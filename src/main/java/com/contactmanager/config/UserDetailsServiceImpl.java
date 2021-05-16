package com.contactmanager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.contactmanager.entities.User;
import com.contactmanager.service.UserService;

public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired 
	UserService userService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = this.userService.fetchUserByUserName(username);
		
		if(user==null) {
			throw new UsernameNotFoundException("Invalid  credentials !!");
		}
		
		CustomUesrDetails customUesrDetails=new CustomUesrDetails(user);
		
		return  customUesrDetails;
	}
	

}

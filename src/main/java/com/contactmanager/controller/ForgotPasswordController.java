package com.contactmanager.controller;

import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.contactmanager.entities.User;
import com.contactmanager.service.EmailService;
import com.contactmanager.service.UserService;



@Controller
public class ForgotPasswordController {
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	Random random = new Random(1000);
	
	//email id form open
	
	@GetMapping("/forgot-password")
	public String openEmailForm(Model model) {
		System.out.println("ForgotPasswordController.openEmailForm()");
		
		model.addAttribute("title", "Forgot password");
		
		return "forgotemail_form";
	}
	
	//otp sending
	
	@PostMapping("/send-otp")
	public String sendOtp(@RequestParam("email") String email,Model model,HttpSession session) {
		
		System.out.println(email);
		model.addAttribute("title", "Verification");
		//otp generation
		
		
		int otp = random.nextInt(9999);
		
		
		System.out.println(otp);
		
		//write code for otp sent to email
		
		String subject="OTP from Contact Manager Pro ";
		String message="<div style='border:1px solid #e2e2e2;padding:20px'>"+
		               "<h2>"+"OTP is "+
		               "</h2>"+"<h1 style='color:blue'>"+otp+
		               "</h1>"+
				       "</div>";
				
		String to=email;
		
		boolean sendEmail = this.emailService.sendEmail(subject, message, to);
		
		
		if(sendEmail) {		
			
			session.setAttribute("sessionOtp",otp);
			session.setAttribute("email",email);
			
			return "verify_otp";
		}
		else {
			session.setAttribute("message","Check your email id....");
			
			return "forgotemail_form";
		}
		
	}
	
	//verify otp
	
	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestParam("otp") int otp,HttpSession session,Model model) {
		
		int sessionOtp=(int) session.getAttribute("sessionOtp");
		String email=(String) session.getAttribute("email");
		
		System.out.println(sessionOtp);
		System.out.println(otp);
		
		if(sessionOtp==otp) {
			
			User user = this.userService.fetchUserByUserName(email);
			
			if(user==null) {
				//error message
				session.setAttribute("message","Email does not exist..");			
				return "forgotemail_form";
			}
			else {
				//password change	
				model.addAttribute("title","Change Password");
				return "password_change_form";	
				
			}
			
		}
		else {
			
			session.setAttribute("message","Enter correct Otp!!");
			return "verify_otp";
		}
		
	}
	
	
	//change password
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("newPassword") String password,HttpSession session) {
		
		String email=(String) session.getAttribute("email");
		User user = this.userService.fetchUserByUserName(email);
		
		user.setUserPassword(this.bCryptPasswordEncoder.encode(password));
		this.userService.saveUserData(user);
		
		return "redirect:/signin?change=Password changed successfully!";
	}
	

}

package com.contactmanager.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.contactmanager.entities.User;
import com.contactmanager.helper.Message;
import com.contactmanager.service.UserService;

@Controller
public class AuthenticationController {

	@Autowired
	private UserService userService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@RequestMapping("/signup")
	public String signupHandler(Model model) {
		System.out.println("AuthenticationController.signupHandler()");
		model.addAttribute("title", "Signup- Contact Manager Pro");
		model.addAttribute("user", new User());
		return "Signup";
	}

	@PostMapping("/do_signup")
	public String signupDataHandler(@Valid @ModelAttribute("user") User user, BindingResult result,
			@RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model,
			HttpSession session) {
		System.out.println("AuthenticationController.signupDataHandler()");

		try {
			if (!agreement) {
				System.out.println("You have not agreed terms and conditions!");
				throw new Exception("You have not agreed terms and conditions!");
			}

			if (result.hasErrors()) {

				System.out.println("Error:" + result.toString());
				model.addAttribute("user", user);
				return "signup";
			}

			user.setUserRole("ROLE_USER");
			user.setUserEnabled(true);
			user.setUserImageUrl("default.png");
			user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));

			// checking if user name exist or not
			User userdb = this.userService.fetchUserByUserName(user.getUserEmail());
			if (userdb!=null) {
				session.setAttribute("message", new Message("User exist with this email!", "alert-warning"));
				return "signup";
			}
			// saving to database
			this.userService.saveUserData(user);
			model.addAttribute("user", new User());
			session.setAttribute("message", new Message("Successfuly registered !", "alert-success"));
			return "signup";

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something went wrong !" + e.getMessage(), "alert-danger"));
			return "signup";
		}

	}

	@GetMapping("/signin")
	public String customLogin(Model m) {

		m.addAttribute("title", "Login - Contact Manager Pro");
		return "login";
	}
}

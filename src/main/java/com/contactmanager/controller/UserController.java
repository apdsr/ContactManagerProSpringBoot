package com.contactmanager.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.contactmanager.entities.Contact;
import com.contactmanager.entities.User;
import com.contactmanager.helper.Message;
import com.contactmanager.service.ContactService;
import com.contactmanager.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private ContactService contactService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	// common data for all handler under UserController

	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		String userName = principal.getName();
		System.out.println(userName);

		// get the user using userName(Email)
		User user = this.userService.fetchUserByUserName(userName);

		model.addAttribute("user", user);
		System.out.println(user);
	}

	// dashboard home

	@GetMapping("/index")
	public String dashboard(Model model) {
		model.addAttribute("title", "User Dashboard");
		return "normal/user_dashboard";
	}

	// open add form handler

	@GetMapping("/add-Contact")
	public String openAddContactForm(Model model) {

		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());

		return "normal/add_contact_form";
	}

	// processing add contact form
	@PostMapping("/process-contact")
	public String processAddContact(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,
			Principal principal, HttpSession sesson) {
		try {
			System.out.println(contact);
			String userName = principal.getName();
			User user = this.userService.fetchUserByUserName(userName);
			System.out.println(user);

			// prevent add duplicate contact with same email
			if (!this.contactService.findByEmailAndUser(contact.getContactEmail(), user).isEmpty()) {

				sesson.setAttribute("message", new Message("Already have contact with this email !!", "warning"));
				return "normal/add_contact_form";
			}

			// processing uploading file
			if (file.isEmpty()) {
				System.out.println("Empty file");
				contact.setContactImageUrl("contact.png");
			} else {
				contact.setContactImageUrl(file.getOriginalFilename());

				File saveFile = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator, file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("file uploaded");
			}

			contact.setUser(user);
			System.out.println("setUser");

			user.getContacts().add(contact);
			System.out.println("getContacts");

			this.userService.saveUserData(user);
			System.out.println("Added to Database");

//		message success
			sesson.setAttribute("message", new Message("Your Contact is Added ! Add new one !!", "success"));

		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();

//			error message
			sesson.setAttribute("message", new Message("Something went wrong ! try Again!!", "danger"));
		}

		return "normal/add_contact_form";
	}

	// show contact
	@GetMapping("/show-contacts/{page}")
	public String showContact(@PathVariable("page") Integer page, Model m, Principal principal) {
		m.addAttribute("title", "Show Contacts");

		// contact list of user

		String userName = principal.getName();
		User user = this.userService.fetchUserByUserName(userName);
		// user.getContacts();

		Page<Contact> contacts = this.contactService.fetchContactsByUser(user.getUserId(), page);

		System.out.println(contacts.isEmpty());
		if (contacts.isEmpty()) {
			return "normal/showempty_contact";
		}

		// System.out.println("Contact:"+fetchContactsByUser.get(0).getContactName());

		m.addAttribute("contacts", contacts);
		m.addAttribute("currentPage", page);
		m.addAttribute("totalPages", contacts.getTotalPages());

		return "normal/show_contacts";
	}

	// showing particular contact details

	@GetMapping("/{cid}/contact")
	public String showContactDetails(@PathVariable("cid") Integer cid, Model model, Principal principal) {

		System.out.println(cid);
		Optional<Contact> optContact = this.contactService.fetchByID(cid);
		Contact contact = optContact.get();

		String userName = principal.getName();
		User user = this.userService.fetchUserByUserName(userName);

		if (user.getUserId() == contact.getUser().getUserId())
			model.addAttribute("contact", contact);

		return "normal/contact_details";
	}

	// delete contact handler
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") Integer cid, Principal principal, HttpSession session) {

		Optional<Contact> fetchByID = this.contactService.fetchByID(cid);
		Contact contact = fetchByID.get();

		String userName = principal.getName();
		User user = this.userService.fetchUserByUserName(userName);

		if (user.getUserId() == contact.getUser().getUserId()) {

			// remove particular contact obj
			user.getContacts().remove(contact);
			// update user
			this.userService.saveUserData(user);

			session.setAttribute("message", new Message("Contact " + contact.getContactName() + " "
					+ contact.getContactSecondname() + " deleted successfully!!", "success"));

		}

		return "redirect:/user/show-contacts/0";
	}

	// open update form handler
	@PostMapping("/update-contact/{cid}")
	public String openUpdateForm(@PathVariable("cid") int cid, Model model) {

		model.addAttribute("titie", "Update Contact");

		Optional<Contact> fetchByID = this.contactService.fetchByID(cid);
		Contact contact = fetchByID.get();
		model.addAttribute("contact", contact);

		return "normal/update_form";
	}

	// update data handler

	@PostMapping("/process-update")
	public String updateData(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file, Model m,
			HttpSession session, Principal principal) {

		System.out.println(contact.getContactId());

		try {

			// old contact details
			Contact oldContact = this.contactService.fetchByID(contact.getContactId()).get();

			if (!file.isEmpty()) {

				// rewrite
				// delete old pic
				File deleteFile = new ClassPathResource("static/img").getFile();
				File file2 = new File(deleteFile, oldContact.getContactImageUrl());
				file2.delete();

				// update new one
				File saveFile = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator, file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				contact.setContactImageUrl(file.getOriginalFilename());

			} else {
				contact.setContactImageUrl(oldContact.getContactImageUrl());
			}
			String userName = principal.getName();
			User user = this.userService.fetchUserByUserName(userName);
			contact.setUser(user);
			this.contactService.saveUpdatedContact(contact);
			session.setAttribute("message", new Message("Updated contact successfully...", "success"));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:/user/" + contact.getContactId() + "/contact";
	}

	// your profile handler

	@GetMapping("/profile")
	public String userProfile(Model model) {

		model.addAttribute("title", "User Profile");
		return "normal/profile";
	}

	// open setting handler
	@GetMapping("/settings")
	public String openSettings(Model model) {

		model.addAttribute("title", "Settings");
		return "normal/settings";
	}

	// change password
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldpassword") String oldpassword,
			@RequestParam("newpassword") String newpassword, Principal principal, HttpSession session) {

		System.out.println(oldpassword + "   " + newpassword);

		String userName = principal.getName();
		User user = this.userService.fetchUserByUserName(userName);
		System.out.println(user.getUserPassword());

		if (this.bCryptPasswordEncoder.matches(oldpassword, user.getUserPassword())) {
			user.setUserPassword(this.bCryptPasswordEncoder.encode(newpassword));
			this.userService.saveUserData(user);
			session.setAttribute("message", new Message("Password changed successfully...", "success"));
		} else {
			session.setAttribute("message", new Message("Invalid password!!", "danger"));
			return "redirect:/user/settings";
		}
		return "redirect:/user/index";
	}

	// delete user
	@GetMapping("/delete-user")
	public String deleteUser(Principal principal) {

		String userName = principal.getName();
		System.out.println(userName);

		// get the user using userName(Email)
		User user = this.userService.fetchUserByUserName(userName);
		this.userService.deleteUser(user);

		return "redirect:/signin";
	}

	// open update user form
	@GetMapping("/update-user")
	public String openUpdateUserForm() {

		return "normal/updateUser_form";
	}

	// update user info
	@PostMapping("/update-userinfo")
	public String UpdateUserInfo(@RequestParam("about") String about, Model model, Principal principal) {

		String username = principal.getName();
		User user = this.userService.fetchUserByUserName(username);
		user.setUserDesc(about);
		this.userService.saveUserData(user);

		return "normal/profile";
	}

}

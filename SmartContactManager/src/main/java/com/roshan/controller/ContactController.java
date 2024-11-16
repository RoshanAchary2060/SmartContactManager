package com.roshan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.roshan.entity.Contacts;
import com.roshan.entity.Users;
import com.roshan.helper.Helper;
import com.roshan.model.Contact;
import com.roshan.service.IContactService;
import com.roshan.service.IImageService;
import com.roshan.service.IUserService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {
	@Autowired
	private IUserService service;

	@Autowired
	private IImageService imageService;

	@Autowired
	private IContactService contactService;

	@ModelAttribute
	public void addLoggedinUserInformation(Model model, Authentication authentication) {
		if (authentication == null) {
			System.out.println("auth is null");
			return;
		}
		String username = Helper.getEmailOfLoggedinUser(authentication);
		Users user = service.getUserByEmail(username);
		if (user == null) {
			model.addAttribute("loggedinuser", null);
		} else {
			System.out.println(user.getName() + ", " + user.getEmail());
			model.addAttribute("loggedinuser", user);
		}
	}

	// add contact page handler
	@GetMapping("/add")
	public String addContactView(Model model, Authentication authentication) {
//		addLoggedinUserInformation(model, authentication);
		Contact contact = new Contact();
		model.addAttribute("contact", contact);
		return "user/add_contact";
	}

	@PostMapping("/add")
	public String saveContact(@Valid @ModelAttribute Contact contact, Model model, Authentication authentication,
			RedirectAttributes attrs) {
//		addLoggedinUserInformation(model, authentication);
		// process the form data
		System.out.println("We are in contactcontroller" + contact);
		Contacts contacts = new Contacts();
		contacts.setName(contact.getName());
		contacts.setFavorite(contact.getFavorite());
		contacts.setEmail(contact.getEmail());
		contacts.setPhoneNumber(contact.getPhoneNumber());
		contacts.setAddress(contact.getAddress());
		contacts.setDescription(contact.getDescription());
		contacts.setWebsiteLink(contact.getWebsiteLink());
		contacts.setFacebookLink(contact.getFacebookLink());
		contacts.setLinkedinLink(contact.getLinkedinLink());

		String username = Helper.getEmailOfLoggedinUser(authentication);
		Users users = service.getUserByEmail(username);

		// image processing

		String fileURL = imageService.uploadImage(contact.getPicture());

		contacts.setUser(users);
		contacts.setPicture(fileURL);
		System.out.println("We are here");
		contactService.save(contacts);
		attrs.addFlashAttribute("message", "Contact saved successfully...");
		System.out.println("Contact saved...");
		return "redirect:/user/contacts/add";
	}

	// view contacts
	@GetMapping()
	public String viewContacts(Model model, Authentication authentication, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "1") int size, @RequestParam(defaultValue = "name") String sortBy,
			@RequestParam(defaultValue = "asc") String direction) {

		// load all the user contacts
		String username = Helper.getEmailOfLoggedinUser(authentication);
		Users user = service.getUserByEmail(username);
		Page<Contacts> pageContact = contactService.getByUser(user, page, size, sortBy, direction);
	

		model.addAttribute("contacts", pageContact);
		return "user/contacts";
	}

	@GetMapping("/search")
	public String searchHandler(
			Authentication authentication,
			@RequestParam String field, @RequestParam("keyword") String value,
			@RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "name") String sortBy, @RequestParam(defaultValue = "asc") String direction,
			Model model) {

		System.out.println("Search method in backend : " + field + "," + value);

		Page<Contacts> pageContacts = null;
		Users user = service.getUserByEmail(Helper.getEmailOfLoggedinUser(authentication));

		if (field.equalsIgnoreCase("name")) {
			pageContacts = contactService.searchByName(value, size, page, sortBy, direction, user);
		} else if (field.equalsIgnoreCase("email")) {
			pageContacts = contactService.searchByEmail(value, size, page, sortBy, direction, user);

		} else if (field.equalsIgnoreCase("phone")) {
			pageContacts = contactService.searchByPhone(value, size, page, sortBy, direction, user);

		}
		model.addAttribute("contacts", pageContacts);
		return "user/search";
	}
}

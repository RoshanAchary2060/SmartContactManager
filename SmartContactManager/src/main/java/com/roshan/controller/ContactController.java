package com.roshan.controller;

import com.roshan.repo.IContactRepo;
import com.roshan.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.roshan.entity.Contacts;
import com.roshan.entity.Users;
import com.roshan.helper.Helper;
import com.roshan.model.Contact;
import com.roshan.service.IContactService;
import com.roshan.service.IImageService;
import com.roshan.service.IUserService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/user/contacts")
public class ContactController {

	@Autowired
	private IContactRepo iContactRepo;
	@Autowired
	private IUserService service;

	@Autowired
	private IImageService imageService;

	@Autowired
	private IContactService contactService;

	@Autowired
	private EmailService emailService;

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

			int contactsCount = getContactCount(user);
			System.out.println("contactscount: " + contactsCount);
			model.addAttribute("count", contactsCount);

			model.addAttribute("loggedinuser", user);
		}
	}
	public int getContactCount(Users user){
		String userId = user.getUserId();

		return (int)iContactRepo.countByUserUserId(userId);
	}

	@GetMapping("/email")
	public String returnEmail(@RequestParam String email, Model model){
		model.addAttribute("email",email);
		return "user/email";
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
			@RequestParam(defaultValue = "2") int size, @RequestParam(defaultValue = "name") String sortBy,
			@RequestParam(defaultValue = "asc") String direction) {
		addLoggedinUserInformation(model, authentication);
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

	// delete contact
	@GetMapping("/delete/{contactId}")
	public String deleteContact(@PathVariable String contactId){
		contactService.delete(contactId);
		log.info("contactId {} deleted", contactId);
		return "redirect:/user/contacts";
	}

	// update contact form
	@GetMapping("/view/{contactId}")
	public String updateContact(@PathVariable String contactId, Model model){
		Contacts contact  = contactService.getById(contactId);
		Contact contact1 = new Contact();
		contact1.setName(contact.getName());
		contact1.setEmail(contact.getEmail());
		contact1.setPhoneNumber(contact.getPhoneNumber());
		contact1.setAddress(contact.getAddress());
		contact1.setDescription(contact.getDescription());
		contact1.setFavorite(contact.isFavorite());
		contact1.setWebsiteLink(contact.getWebsiteLink());
		contact1.setFacebookLink(contact.getFacebookLink());
		contact1.setLinkedinLink(contact.getLinkedinLink());
		contact1.setPicturePath(contact.getPicture());
		model.addAttribute("contact", contact1);
		model.addAttribute("contactId", contactId);
		return "user/update_contact_view";
	}

	@PostMapping("/update/{contactId}")
	public String updateContact(@PathVariable String contactId, @ModelAttribute Contact contact, RedirectAttributes attrs, Authentication authentication, @RequestParam(required = false)MultipartFile image){
		Contacts contact2 = new Contacts();
		contact2.setId(contactId);
		contact2.setFavorite(contact.getFavorite());
		contact2.setName(contact.getName());
		contact2.setEmail(contact.getEmail());
		contact2.setPhoneNumber(contact.getPhoneNumber());
		if(!image.isEmpty()) {

			String fileURL = imageService.uploadImage(image);
			contact2.setPicture(fileURL);
		} else {
			Contacts contact3 = iContactRepo.findById(contactId).get();
			System.out.println("picture:"+contact3.getPicture());
			contact2.setPicture(contact3.getPicture());
		}
		contact2.setAddress(contact.getAddress());
		contact2.setWebsiteLink(contact.getWebsiteLink());
		contact2.setLinkedinLink(contact.getLinkedinLink());
		contact2.setFacebookLink(contact.getFacebookLink());
		contact2.setDescription(contact.getDescription());
		Users user = service.getUserByEmail(Helper.getEmailOfLoggedinUser(authentication));

		contact2.setUser(user);
		// update the contact
		contactService.update(contact2);
		attrs.addFlashAttribute("message", "Contact updated successfully...");
		System.out.println("Contact updated...");
		return "redirect:/user/contacts/view/"+contactId;
	}



	@GetMapping("/send-email")
	public String sendEmail(@RequestParam String email, @RequestParam String subject,
							@RequestParam String message,  Model model,
							Authentication authentication) {
		try {
			System.out.println("Inside API " + email);
			emailService.sendEmail(email, subject, message);

			// Add success message or any required info
			model.addAttribute("message", "Email successfully sent!");
		} catch (Exception e) {
			model.addAttribute("message", "Failed to send email!");
		}
		System.out.println("Ready to return");

		// Ensure the contacts page model data is passed over with the redirect
		addLoggedinUserInformation(model, authentication);
		String username = Helper.getEmailOfLoggedinUser(authentication);
		Users user = service.getUserByEmail(username);
		Page<Contacts> pageContact = contactService.getByUser(user, 0, 1, "name", "asc");

		// Adding pageContact and other required attributes to attrs to be passed with redirect
		model.addAttribute("contacts", pageContact);

		return "redirect:/user/contacts";
	}
}

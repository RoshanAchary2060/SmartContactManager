package com.roshan.controller;

import com.roshan.model.EmailRequest;
import com.roshan.service.EmailService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.roshan.entity.Contacts;
import com.roshan.service.IContactService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/api")
public class ApiController {
	
	@Autowired
	private IContactService service;

	@ResponseBody
	@GetMapping("/contacts/{contactId}")
	public Contacts getContact(@PathVariable String contactId) {
		return service.getById(contactId);
	}




	// Endpoint to send the email
//	@GetMapping("/send-email")
//	public String sendEmail(@RequestParam String email, @RequestParam String subject,
//							@RequestParam String message, RedirectAttributes attrs
//	, Model model) {
//		try {
//			System.out.println("Inside api " +  email);
//			emailService.sendEmail(email, subject, message);
//			attrs.addFlashAttribute("message","Email successfully sent...");
//		} catch (Exception e) {
//			attrs.addFlashAttribute("message","Failed to send email!");
//		}
//		System.out.println("ready to return");
//
//
//		model.addAttribute("email", email);
//		return "/user/email";
//
//	}\



}

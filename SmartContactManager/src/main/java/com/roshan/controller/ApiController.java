package com.roshan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.roshan.entity.Contacts;
import com.roshan.service.IContactService;

@RestController
@RequestMapping("/api")
public class ApiController {
	
	@Autowired
	private IContactService service;

	@GetMapping("/contacts/{contactId}")
	public Contacts getContact(@PathVariable String contactId) {
		return service.getById(contactId);
	}

}

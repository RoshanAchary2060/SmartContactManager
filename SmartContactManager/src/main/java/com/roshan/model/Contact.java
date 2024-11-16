package com.roshan.model;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
	private String name = "Roshan";
	private String email;
	private String phoneNumber;
	private String address;
	private String description;
	
	
	private MultipartFile picture;
	
	private boolean favorite ;
	public boolean getFavorite() {
		return this.favorite;
	}
	private String websiteLink;
	private String linkedinLink;
	private String facebookLink;
}

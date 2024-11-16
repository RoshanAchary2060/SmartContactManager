package com.roshan.service;

import org.springframework.web.multipart.MultipartFile;

public interface IImageService {
	
	public String uploadImage(MultipartFile file);
	
	public String getUrlFromPublicId(String publicId);
}

package com.roshan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@SpringBootApplication
public class SmartContactManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartContactManagerApplication.class, args);
	}
//	@Bean
//	public JavaMailSender mailSender() {
//		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//		mailSender.setHost("smtp.gmail.com"); // SMTP host
//		mailSender.setPort(587); // SMTP port
//		mailSender.setUsername("rosanacharya2060@gmail.com"); // Email username
//		mailSender.setPassword("rosanpro.com.np@325"); // Email password
//
//		// Set additional properties
//		java.util.Properties props = mailSender.getJavaMailProperties();
//		props.put("mail.smtp.auth", "true");
//		props.put("mail.smtp.starttls.enable", "true");
//		props.put("mail.smtp.timeout", "5000");
//
//		return mailSender;
//	}

}

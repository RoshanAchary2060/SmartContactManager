package com.roshan.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.roshan.entity.Users;
import com.roshan.helper.Message;
import com.roshan.helper.MessageType;
import com.roshan.model.User;
import com.roshan.service.IUserService;

import jakarta.validation.Valid;
@Controller
public class PageController {
    @Autowired
    private IUserService service;
    @Autowired
    private PasswordEncoder encoder;
    @GetMapping("/home")
    public String showHome(Model model) {
        model.addAttribute("name", "Rn Dai");
        return "home";
    }
    @GetMapping()
    public String showHome2(Model model) {
        model.addAttribute("name", "Rn Dai");
        return "home";
    }
    @GetMapping("/about")
    public String showAbout() {
        return "about";
    }
    @GetMapping("/navbar")
    public String showNavbar() {
        return "navbar";
    }
    @GetMapping("/services")
    public String showServices() {
        return "services";
    }
    @GetMapping("/contact")
    public String showContact() {
        return "contact";
    }
    @GetMapping("/login")
    public String showLogin(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "login";
    }
    @GetMapping("/register")
    public String showRegister(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }
    @PostMapping("/authenticate")
    public String processLogin(@ModelAttribute @Valid User user, BindingResult result, Model model,
                               RedirectAttributes attrs) {
        try {
            // Check for validation errors from the form
            if (result.hasErrors()) {
                // If form validation fails, return to the registration page with the error
                // message
                model.addAttribute("user", user);
                Message message = Message.builder().content("Login Failed! Please correct the errors.")
                        .type(MessageType.red).build();
                attrs.addFlashAttribute("message", message);
                return "redirect:/login";
            }
            // Check if the email already exists using the isUserExistByEmail method
            if (service.isUserExistByEmail(user.getEmail())) {
                System.out.println("Email is existing");
                // If the email exists, return to the registration page with a specific message
                String enteredPassword = user.getPassword();
                Users user2 = service.getUserByEmail(user.getEmail());
                String dbPassword = user2.getPassword();
                if (encoder.matches(enteredPassword, dbPassword)) {
                    attrs.addFlashAttribute("object", user2);
                    System.out.println("We should go to user/dashboard");
                    return "redirect:/user/dashboard";
                } else {
                    model.addAttribute("user", user);
                    Message message = Message.builder().content("Wrong password! Try again.")
                            .type(MessageType.red).build();
                    attrs.addFlashAttribute("message", message);
                    return "redirect:/login";
                }
            } else {
                model.addAttribute("user", user);
                Message message = Message.builder().content("Login Failed! Please correct the errors.")
                        .type(MessageType.red).build();
                attrs.addFlashAttribute("message", message);
                return "redirect:/login";
            }
        } catch (Exception ex) {
            // Catch any other exceptions (e.g., database errors) and show a general error
            // message
            Message message = Message.builder().content("Login Failed! Please try again.").type(MessageType.red)
                    .build();
            attrs.addFlashAttribute("message", message);
            return "redirect:/login";
        }
    }
    @PostMapping("/do_register")
    public String processRegister(@ModelAttribute @Valid User user, BindingResult result, Model model,
                                  RedirectAttributes attrs) {
        try {
            // Check for validation errors from the form
            if (result.hasErrors()) {
                // If form validation fails, return to the registration page with the error
                // message
                model.addAttribute("user", user);
                Message message = Message.builder().content("Registration Failed! Please correct the errors.")
                        .type(MessageType.red).build();
                attrs.addFlashAttribute("message", message);
                return "redirect:/register";
            }
            // Check if the email already exists using the isUserExistByEmail method
            if (service.isUserExistByEmail(user.getEmail())) {
                System.out.println("Email is existing");
                // If the email exists, return to the registration page with a specific message
                model.addAttribute("user", user);
                Message message = Message.builder().content("Registration Failed! Email already exists.")
                        .type(MessageType.red).build();
                attrs.addFlashAttribute("message", message);
                return "redirect:/register";
            }
            // Proceed with saving the new user
            Users newUser = new Users();
            newUser.setName(user.getName());
            newUser.setEmail(user.getEmail());
            newUser.setPassword(user.getPassword());
            newUser.setAbout(user.getAbout());
            newUser.setPhoneNumber(user.getPhoneNumber());
            newUser.setProfilePic(
                    "https://www.holy-bhagavad-gita.org/public/images/about/Jagadguru-Shri-Kripaluji-Maharaj-2.jpg");
            Users savedUser = service.saveUser(newUser);
            if (savedUser != null) {
                // If the user was saved successfully, redirect with a success message
                Message message = Message.builder().content("Registration Successful").type(MessageType.green).build();
                attrs.addFlashAttribute("message", message);
                return "redirect:/register"; // Redirect to the login page (or any other page you prefer)
            } else {
                // If saving the user fails for any reason, return with a generic failure
                // message
                Message message = Message.builder().content("Registration Failed! Please try again.")
                        .type(MessageType.red).build();
                attrs.addFlashAttribute("message", message);
                return "redirect:/register";
            }
        } catch (Exception ex) {
            // Catch any other exceptions (e.g., database errors) and show a general error
            // message
            Message message = Message.builder().content("Registration Failed! Please try again.").type(MessageType.red)
                    .build();
            attrs.addFlashAttribute("message", message);
            return "redirect:/register";
        }
    }
}
package com.roshan.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import com.roshan.entity.Users;
import com.roshan.helper.Helper;
import com.roshan.service.IUserService;
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService service;
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
    // user dashboard page
    @GetMapping("/dashboard")
    public String userDashboard(Model model, Authentication authentication) {
        addLoggedinUserInformation(model, authentication);
        return "user/dashboard";
    }
    @GetMapping("/profile")
    public String userProfile(Authentication authentication, Model model) {
        addLoggedinUserInformation(model, authentication);
        return "user/profile";
    }
    // user add contacts page
}
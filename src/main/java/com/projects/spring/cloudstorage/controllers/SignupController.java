package com.projects.spring.cloudstorage.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.projects.spring.cloudstorage.models.User;
import com.projects.spring.cloudstorage.services.UserService;

@Controller
@RequestMapping("/signup")
public class SignupController {
    UserService userService;

    public SignupController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String signupView(User user, Model model) {

        return "signup";
    }

    @PostMapping
    public String signupUser(User user, Model model) {
        String errorMsg = null;

        if(!userService.isUsernameAvailable(user.getUsername())) {
            errorMsg = "This username is already taken!";
        }

        if(errorMsg == null) {
            int rowsAdded = userService.createUser(user);
            if (rowsAdded < 0) {
                errorMsg = "Something went wrong, unable to sign up";
            }
        }

        if(errorMsg == null) {
            model.addAttribute("signupSuccess", true);
        } else {
            model.addAttribute("errorInSignup", true);
            model.addAttribute("errorMsg", errorMsg);
        }

        return "signup";
    }
}

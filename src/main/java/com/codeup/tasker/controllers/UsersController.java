package com.codeup.tasker.controllers;

import com.codeup.tasker.models.User;
import com.codeup.tasker.repos.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UsersController {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public UsersController(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String userRegistrationForm(Model model){
        model.addAttribute("register", new User());
        return "register-page";
    }

    @PostMapping("/register")
    public String userRegistrationSubmit(@ModelAttribute User user){
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        userRepo.save(user);
        return "/login";
    }


}

package com.codeup.tasker.controllers;

import com.codeup.tasker.models.User;
import com.codeup.tasker.repos.UserRepo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

//    ******* VIEW PROFILE PAGE ***********
//    @GetMapping("/profile")
//    public String viewProfileLink(Model model){
//        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        User user = userRepo.findById(loggedInUser.getId()).get();
//        System.out.println(user.getFullName());
//        model.addAttribute("viewProfile", loggedInUser);
//        return "/users/profile";
//    }

    @GetMapping("/profile/{id}")
    public String viewProfile(Model model, @PathVariable Long id){
//        User loggedInUser = userRepo.findById(id).get();
//        System.out.println(loggedInUser.getFullName());
        model.addAttribute("viewProfile", userRepo.findById(id).get());
        return "/users/profile";
    }

//    ******* EDIT PROFILE MAPPINGS *********
    @GetMapping("/profile/{id}/edit")
    public String profileEdit(@PathVariable Long id, Model model){
        User user = userRepo.findById(id).get();
        model.addAttribute("fullName", user.getFullName());
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("userId", user.getId());
        return "/users/profile-edit";
    }

    @PostMapping("/profile/{id}/edit")
    public String profileEditSubmit(@PathVariable Long id, @RequestParam String fullName, @RequestParam String username, @RequestParam String email ){
        User loggedInUser = userRepo.findById(id).get();
        loggedInUser.setId(id);
        loggedInUser.setFullName(fullName);
        loggedInUser.setUsername(username);
        loggedInUser.setEmail(email);
        userRepo.save(loggedInUser);
        return "redirect:/profile/{id}";
    }

//    ****** EDIT USER PASSWORD MAPPINGS ********

    @GetMapping("/profile/{id}/pwEdit")
    public String editPwForm(@PathVariable Long id, Model model){
        User userPw =  userRepo.findById(id).get();
        model.addAttribute("editPassword", userPw.getPassword());
        model.addAttribute("userId", userPw.getId());
        return "users/edit-pw-form";
    }

    @PostMapping("/profile/{id}/pwEdit")
    public String editPwSubmit(@PathVariable Long id, @RequestParam String password){
        User loggedInUser = userRepo.findById(id).get();
        String hashedPassword = passwordEncoder.encode(password);
        loggedInUser.setId(id);
        loggedInUser.setPassword(hashedPassword);

        userRepo.save(loggedInUser);
        return "redirect:/profile/{id}";
    }


}

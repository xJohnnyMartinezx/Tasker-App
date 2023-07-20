package com.codeup.tasker.controllers;

import com.codeup.tasker.helper.Message;
import com.codeup.tasker.models.User;
import com.codeup.tasker.repos.UserRepo;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.expression.Messages;

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
//        return "/partials/nav";
    }

//    ******* EDIT PROFILE MAPPINGS *********
    @GetMapping("/profile/{id}/edit")
    public String profileEdit(@PathVariable Long id, Model model){
        User user = userRepo.findById(id).get();
//        model.addAttribute("profileEditBtn", user);
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
        model.addAttribute("currentPw", userPw.getPassword());
        model.addAttribute("userIdForPw", userPw.getId());
//        String pwCheck = passwordEncoder.matches(userPw, userPw.getPassword());
        return "users/edit-pw-form";
    }

//    @PostMapping("/profile/{id}/pwEdit")
//    public String editPwSubmit(@PathVariable Long id, @RequestParam String currentPw, @RequestParam String newPassword){
//        User loggedInUser = userRepo.findById(id).get();
//        boolean pwCheck = passwordEncoder.matches(currentPw, loggedInUser.getPassword());
//        try {
//
//            if (pwCheck) {
//                System.out.println("Current PW Matches: " + currentPw);
//                String hashedPassword = passwordEncoder.encode(newPassword);
//                loggedInUser.setId(id);
//                loggedInUser.setPassword(hashedPassword);
//                userRepo.save(loggedInUser);
//
//                return "redirect:/profile/{id}";
//            } else {
//                System.out.println("No Match");
//                return "redirect:/profile/{id}/pwEdit";
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return "redirect:/profile/{id}/pwEdit";
//    }

    @PostMapping("/profile/{id}/pwEdit")
    public String editPwSubmit(@PathVariable Long id, @RequestParam String currentPw, @RequestParam String newPassword, HttpSession session){
        User loggedInUser = userRepo.findById(id).get();
        boolean pwCheck = passwordEncoder.matches(currentPw, loggedInUser.getPassword());
        try {

            if (pwCheck) {
                System.out.println("Current PW Matches: " + currentPw);
                String hashedPassword = passwordEncoder.encode(newPassword);
                loggedInUser.setId(id);
                loggedInUser.setPassword(hashedPassword);
                userRepo.save(loggedInUser);
                session.setAttribute("message", new Message("Your password has been successfully updated!", "success"));

                return "redirect:/pwSuccess/{id}";
            } else {
                System.out.println("No Match");
                session.setAttribute("message", new Message("Your old password doesn't match!", "danger"));
                return "redirect:/profile/{id}/pwEdit";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/profile/{id}/pwEdit";
    }

    @GetMapping("/pwSuccess/{id}")
    public String successfulPwUpdate(@PathVariable Long id, Model model){
        User loggedInUser = userRepo.findById(id).get();
    model.addAttribute("userId", loggedInUser.getId());
        return "users/pw-update-success";
    }

//    @PostMapping("/profile/{id}/pwEdit")
//    public String editPwSubmit(@PathVariable Long id, @RequestParam String currentPw, @RequestParam String newPassword, @Valid @ModelAttribute User user, BindingResult bindingResult, Model model){
//        User loggedInUser = userRepo.findById(id).get();
//        boolean pwCheck = passwordEncoder.matches(currentPw, loggedInUser.getPassword());
//
//        if (pwCheck){
//            System.out.println("Current PW Matches: " + currentPw);
//            String hashedPassword = passwordEncoder.encode(newPassword);
//            loggedInUser.setId(id);
//            loggedInUser.setPassword(hashedPassword);
//            userRepo.save(loggedInUser);
//
//            return "redirect:/profile/{id}";
//        }else {
//            if (bindingResult.hasErrors()) {
//                System.out.println("No Match");
//                model.addAttribute("message", "Registration unsuccessful, old password doesn't match ");
//            }
//            return "redirect:/profile/{id}/pwEdit";
//        }
//
//    }


}

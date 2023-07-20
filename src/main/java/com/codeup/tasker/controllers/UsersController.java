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

@Controller
public class UsersController {

//    *** DEPENDENCY INJECTION AND CONSTRUCTOR ****
//    *********************************************
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public UsersController(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

//    ******* NEW USER REGISTRATION MAPPINGS ******
//    *********************************************
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

//    ******* VIEW PROFILE PAGE MAPPING ***********
//    *********************************************
    @GetMapping("/profile/{id}")
    public String viewProfile(Model model, @PathVariable Long id, HttpSession session){
        model.addAttribute("viewProfile", userRepo.findById(id).get());
//        **** vvv WILL REMOVE "UserEditMessage" ATTRIBUTES BEING CREATED IN "/profile/{id}/edit" POST MAPPING;
        session.removeAttribute("UserEditMessage");
        return "/users/profile";
    }

//    ******* EDIT PROFILE MAPPINGS *********
//    ***************************************
    @GetMapping("/profile/{id}/edit")
    public String profileEdit(@PathVariable Long id, Model model, HttpSession session){
        User user = userRepo.findById(id).get();
        model.addAttribute("fullName", user.getFullName());
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("userId", user.getId());
//        **** vvv WILL REMOVE "pWEditMessage" ATTRIBUTES BEING CREATED IN "/profile/{id}/pwEdit" POST MAPPING;
        session.removeAttribute("pWEditMessage");
        return "/users/profile-edit";
    }

    @PostMapping("/profile/{id}/edit")
    public String profileEditSubmit(@PathVariable Long id, @RequestParam String fullName, @RequestParam String username, @RequestParam String email, HttpSession session){
        User loggedInUser = userRepo.findById(id).get();
            try {
                boolean user = SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
                    if (user) {
                        loggedInUser.setId(id);
                        loggedInUser.setFullName(fullName);
                        loggedInUser.setUsername(username);
                        loggedInUser.setEmail(email);
                        userRepo.save(loggedInUser);
                        session.setAttribute("UserEditMessage", new Message("Your profile has been successfully updated!", "success"));
                        return "redirect:/updateSuccess/{id}";
                    } else {
                        throw new Exception("Something went wrong, try again.");
                    }
            } catch (Exception e){
                String error = e.getMessage();
                session.setAttribute("UserEditMessage", new Message(error ,"danger"));
            }
            return "redirect:/profile/{id}/edit";
    }

//    ****** EDIT USER PASSWORD MAPPINGS ********
//    *******************************************
    @GetMapping("/profile/{id}/pwEdit")
    public String editPwForm(@PathVariable Long id, Model model){
        User userPw =  userRepo.findById(id).get();
        model.addAttribute("currentPw", userPw.getPassword());
        model.addAttribute("userIdForPw", userPw.getId());
        return "users/edit-pw-form";
    }

    @PostMapping("/profile/{id}/pwEdit")
    public String editPwSubmit(@PathVariable Long id, @RequestParam String currentPw, @RequestParam String newPassword, HttpSession session){
        //GETTING THE LOGGED-IN USER;
        User loggedInUser = userRepo.findById(id).get();
        boolean user = SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
        if (user) {
            try {
                //USING "MATCHES" METHODS TO CHECK CURRENT PW ENTERED BY THE USER MATCHES WHATS IN THE DB;
                boolean pwCheck = passwordEncoder.matches(currentPw, loggedInUser.getPassword());
                //IF CURRENT PW (OLD PW) MATCHES, THEN CONTINUE WITH NEW PW UPDATE PROCESS, ELSE THROW EXCEPTION;
                if (pwCheck) {
                    System.out.println("Current PW Matches: " + currentPw);
                    //HASHING NEW PW
                    String hashedPassword = passwordEncoder.encode(newPassword);
                    //SETTING THE CURRENT USER ID...TO RETAIN THE SAME USER ID IN PW UPDATE PROCESS;
                    loggedInUser.setId(id);
                    //SETTING THE NEW HASHED PW;
                    loggedInUser.setPassword(hashedPassword);
                    //SAVING THE NEW PW AND USER ID TO THE DB;
                    userRepo.save(loggedInUser);
                    //DISPLAYING A SUCCESS MSG TO THE USER VIA session.setAttribute AND THYMELEAF
                    session.setAttribute("pWEditMessage", new Message("Your password has been successfully updated!", "success"));
                    return "redirect:/updateSuccess/{id}";
                } else {
                    throw new Exception("Your old password doesn't match!");
                }
            } catch (Exception e) {
                //.getMessage CONVERTS e TO STRING;
                String error = e.getMessage();
                //DISPLAYING A ERROR MSG FROM THE THROWN EXCEPTION TO THE USER VIA session.setAttribute AND THYMELEAF
                session.setAttribute("pWEditMessage", new Message(error, "danger"));
            }
        } else {
            System.out.println("User is unauthorized!");
        }
        return "redirect:/profile/{id}/pwEdit";
    }

//    ****** SUCCESSFUL UPDATE GET MAPPING ******
//    *******************************************
    @GetMapping("/updateSuccess/{id}")
    public String successfulPwUpdate(@PathVariable Long id, Model model){
        User loggedInUser = userRepo.findById(id).get();
        model.addAttribute("userId", loggedInUser.getId());
        return "users/update-success";
    }

}

package com.codeup.tasker.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class AuthenticationController {

    @GetMapping("/login")
    public String showLoginForm(){
        return "/login";
    }

}

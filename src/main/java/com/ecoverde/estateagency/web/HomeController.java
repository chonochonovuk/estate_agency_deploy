package com.ecoverde.estateagency.web;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String index(){
        return "index";
    }


    // Login form
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Login form with error
    @PostMapping("/login-error")
    public String loginError(@ModelAttribute(UsernamePasswordAuthenticationFilter.
            SPRING_SECURITY_FORM_USERNAME_KEY) String username,Model model) {
        model.addAttribute("loginError", true);
        model.addAttribute("username",username);
        return "login";
    }
}

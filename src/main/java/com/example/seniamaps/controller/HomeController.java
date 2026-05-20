package com.example.seniamaps.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(Model model) {

        Authentication auth =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String username = auth.getName();

        model.addAttribute("username", username);

        return "home";
    }
}
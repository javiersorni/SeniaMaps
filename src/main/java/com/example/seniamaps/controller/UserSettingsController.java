package com.example.seniamaps.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.seniamaps.entity.Usuario;
import com.example.seniamaps.repository.UsuarioRepository;

@Controller
@RequestMapping("/settings")
public class UserSettingsController {

    private final UsuarioRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserSettingsController(
            UsuarioRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String settings(Authentication authentication, Model model) {

        Usuario user = userRepository
                .findByUsername(authentication.getName())
                .orElseThrow();

        model.addAttribute("username", user.getUsername());
        model.addAttribute("user", user);

        return "settings";
    }

    @PostMapping("/username")
    public String updateUsername(
            Authentication authentication,
            @RequestParam String username) {

        Usuario user = userRepository
                .findByUsername(authentication.getName())
                .orElseThrow();

        user.setUsername(username);

        userRepository.save(user);

        return "redirect:/settings?usernameUpdated";
    }

    @PostMapping("/password")
    public String updatePassword(
            Authentication authentication,
            @RequestParam String password) {

        Usuario user = userRepository
                .findByUsername(authentication.getName())
                .orElseThrow();

        user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);

        return "redirect:/settings?passwordUpdated";
    }
}
package com.example.seniamaps.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.seniamaps.entity.Usuario;
import com.example.seniamaps.repository.UsuarioRepository;

@Controller
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 🔥 LOGIN
    @GetMapping("/login")
    public String login(Authentication auth) {

        // Si ya está logueado → no volver al login
        if (auth != null && auth.isAuthenticated()
                && !auth.getName().equals("anonymousUser")) {
            return "redirect:/home";
        }

        return "login";
    }

    // 🔥 REGISTER PAGE
    @GetMapping("/register")
    public String register(Authentication auth) {

        if (auth != null && auth.isAuthenticated()
                && !auth.getName().equals("anonymousUser")) {
            return "redirect:/home";
        }

        return "register";
    }

    // 🔥 REGISTER ACTION
    @PostMapping("/register")
    public String registerUser(String username, String password, String email) {

        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setEmail(email);
        usuario.setPassword(passwordEncoder.encode(password));

        usuarioRepository.save(usuario);

        return "redirect:/login?registered";
    }
}
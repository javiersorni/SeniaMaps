package com.example.seniamaps.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

    //LOGIN
    @GetMapping("/login")
    public String login(Authentication auth) {

        //if the user is logged
        if (auth != null && auth.isAuthenticated()
                && !auth.getName().equals("anonymousUser")) {
            return "redirect:/home";
        }

        return "login";
    }

    //REGISTER PAGE
    @GetMapping("/register")
    public String register(Authentication auth) {

        if (auth != null && auth.isAuthenticated()
                && !auth.getName().equals("anonymousUser")) {
            return "redirect:/home";
        }

        return "register";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute("usuario") Usuario usuario, BindingResult result) {
        
        try {
            
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            usuarioRepository.save(usuario); 
            
            return "redirect:/login?registroExitoso";
            
        } catch (DataIntegrityViolationException e) {
            //error message
            String mensajeError = e.getMostSpecificCause().getMessage();
            
            if (mensajeError.contains("username")) {
                result.rejectValue("username", "error.usuario", "El nombre de usuario ya está en uso.");
            } else if (mensajeError.contains("email")) {
                result.rejectValue("email", "error.usuario", "El correo electrónico ya está registrado.");
            } else {
                result.rejectValue("global", "error.usuario", "Error de duplicidad en los datos.");
            }
            
            //show the message
            return "registro"; 
        }
    }
}
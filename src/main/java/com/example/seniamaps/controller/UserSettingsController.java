package com.example.seniamaps.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        model.addAttribute("username", user.getUsername());
        model.addAttribute("user", user);

        return "settings";
    }

    @PostMapping("/username")
    public String cambiarUsername(@RequestParam("username") String nuevoUsername, 
                                Principal principal,
                                RedirectAttributes redirectAttributes) {
        
        //security control
        if (principal == null) {
            return "redirect:/login";
        }
        
        //obtain name
        String usernameActual = principal.getName();
        
        //search user
        Usuario usuario = userRepository.findByUsername(usernameActual)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Usuario no encontrado"));
        
        //check username
        if (userRepository.existsByUsername(nuevoUsername) && !nuevoUsername.equals(usernameActual)) {
            redirectAttributes.addFlashAttribute("errorUsername", "El nombre de usuario ya está en uso.");
            return "redirect:/settings";
        }
        
        //update database
        usuario.setUsername(nuevoUsername);
        userRepository.save(usuario);
        
        //update Spring security
        Authentication nuevaAuth = new UsernamePasswordAuthenticationToken(
                nuevoUsername, 
                usuario.getPassword(), 
                SecurityContextHolder.getContext().getAuthentication().getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(nuevaAuth);
        
        redirectAttributes.addFlashAttribute("exitoUsername", "¡Nombre de usuario actualizado con éxito!");
        return "redirect:/settings";
    }

    @PostMapping("/password")
    public String updatePassword(Authentication authentication, @RequestParam String password, RedirectAttributes redirectAttributes) {
        Usuario user = userRepository
                .findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        //validate password
        if (password == null || password.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorPassword", "La contraseña no puede estar vacía.");
            return "redirect:/settings";
        }

        //encrypt and save password 
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        redirectAttributes.addFlashAttribute("exitoPassword", "Contraseña actualizada correctamente.");
        return "redirect:/settings";
    }
}
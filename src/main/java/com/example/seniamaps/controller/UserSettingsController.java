package com.example.seniamaps.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String cambiarUsername(@RequestParam String username, RedirectAttributes redirectAttributes) {
        try {
            // 1. Recuperamos el usuario actual autenticado
            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            Usuario user = userRepository.findByUsername(currentUsername)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            // 2. Comprobamos si el nombre realmente ha cambiado
            if (!user.getUsername().equals(username)) {
                user.setUsername(username);
                userRepository.save(user);
                
                redirectAttributes.addFlashAttribute("exitoUsername", "Nombre de usuario actualizado correctamente.");
            } else {
                // Si el nombre es exactamente igual al que ya tenía
                redirectAttributes.addFlashAttribute("infoUsername", "No se han detectado cambios en tu nombre de usuario.");
            }
            
            return "redirect:/settings";

        } catch (DataIntegrityViolationException e) {
            // Capturamos si el nombre de usuario ya está cogido en la Base de Datos
            redirectAttributes.addFlashAttribute("errorUsername", "Ese nombre de usuario ya está cogido por otra persona.");
            return "redirect:/settings";
        }
    }

    @PostMapping("/password")
    public String updatePassword(Authentication authentication, @RequestParam String password, RedirectAttributes redirectAttributes) {
        Usuario user = userRepository
                .findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Validamos si la contraseña viene vacía para que no guarde un hash en blanco accidentalmente
        if (password == null || password.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorPassword", "La contraseña no puede estar vacía.");
            return "redirect:/settings";
        }

        // Encriptamos y guardamos la nueva contraseña utilizando tu passwordEncoder
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        // Al cambiar de contraseña, lo ideal es mandar un flash indicando el cambio
        redirectAttributes.addFlashAttribute("exitoPassword", "Contraseña actualizada correctamente.");
        return "redirect:/settings";
    }
}
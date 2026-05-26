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
                                Principal principal, // <--- Usar Principal es más seguro y directo
                                RedirectAttributes redirectAttributes) {
        
        // 1. Control de seguridad preventiva: Si por lo que sea no hay sesión, redirigir al login
        if (principal == null) {
            return "redirect:/login";
        }
        
        // 2. Obtener el nombre actual de la sesión de forma segura
        String usernameActual = principal.getName();
        
        // 3. Buscar el usuario en la base de datos
        Usuario usuario = userRepository.findByUsername(usernameActual)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Usuario no encontrado"));
        
        // 4. Comprobar si el nuevo nombre ya está cogido por OTRA persona
        if (userRepository.existsByUsername(nuevoUsername) && !nuevoUsername.equals(usernameActual)) {
            redirectAttributes.addFlashAttribute("errorUsername", "El nombre de usuario ya está en uso.");
            return "redirect:/settings";
        }
        
        // 5. Actualizar en la base de datos
        usuario.setUsername(nuevoUsername);
        userRepository.save(usuario);
        
        // 6. Actualizar el contexto de Spring Security para que la sesión no se rompa
        Authentication nuevaAuth = new UsernamePasswordAuthenticationToken(
                nuevoUsername, 
                usuario.getPassword(), 
                SecurityContextHolder.getContext().getAuthentication().getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(nuevaAuth);
        
        // 7. Todo ha ido bien
        redirectAttributes.addFlashAttribute("exitoUsername", "¡Nombre de usuario actualizado con éxito!");
        return "redirect:/settings";
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
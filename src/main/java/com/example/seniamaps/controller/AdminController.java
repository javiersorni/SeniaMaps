package com.example.seniamaps.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.seniamaps.entity.Usuario;
import com.example.seniamaps.repository.BusquedaRepository;
import com.example.seniamaps.repository.UsuarioRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final BusquedaRepository busquedaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminController(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            BusquedaRepository busquedaRepository

    ) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.busquedaRepository = busquedaRepository;
    }

    /*
     * =====================================================
     * UPDATE ADMIN SETTINGS
     * =====================================================
     */
    @PostMapping("/settings/update")
    public String updateAdmin(
            String username,
            String password,
            Authentication auth) {

        Usuario admin = usuarioRepository
                .findByUsername(auth.getName())
                .orElseThrow();

        admin.setUsername(username);

        if (password != null && !password.isBlank()) {
            admin.setPassword(passwordEncoder.encode(password));
        }

        usuarioRepository.save(admin);

        return "redirect:/admin/home";
    }

    /*
     * =====================================================
     * ADMIN DASHBOARD
     * =====================================================
     */
    @GetMapping("/home")
    public String adminHome() {
        return "admin/dashboard";
    }

    @GetMapping("/users/create")
    public String createUserForm() {
        return "admin/create-user";
    }

    @PostMapping("/users/create")
    public String createUser(String username, String email, String password) {

        Usuario user = new Usuario();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRol("ROLE_USER");

        usuarioRepository.save(user);

        return "redirect:/admin/users";
    }

    @GetMapping("/users")
    public String usersPage(org.springframework.ui.Model model) {

        model.addAttribute(
                "users",
                usuarioRepository.findAll());

        return "admin/users";
    }

    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/users/history")
    public String usersHistory(Model model) {

        model.addAttribute(
                "historial",
                busquedaRepository.findAll());

        return "admin/users-history";
    }
}
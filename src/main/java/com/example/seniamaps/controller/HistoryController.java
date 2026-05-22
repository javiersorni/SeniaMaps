package com.example.seniamaps.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.seniamaps.entity.Usuario;
import com.example.seniamaps.repository.UsuarioRepository;
import com.example.seniamaps.services.HistorialService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Controller
public class HistoryController {

    private final UsuarioRepository usuarioRepository;
    private final HistorialService historialService;

    public HistoryController(
            UsuarioRepository usuarioRepository,
            HistorialService historialService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.historialService = historialService;
    }

    @GetMapping("/history")
    public String history(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User user,
            Model model
    ) {

        Usuario usuario = usuarioRepository
                .findByUsername(user.getUsername())
                .orElseThrow();

        model.addAttribute("username", user.getUsername());

        model.addAttribute(
                "historial",
                historialService.getHistorialUsuario(usuario)
        );

        return "history";
    }
}
package com.example.seniamaps.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.seniamaps.entity.Usuario;
import com.example.seniamaps.repository.UsuarioRepository;
import com.example.seniamaps.services.HistorialService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
public class HistoryController {

        private final UsuarioRepository usuarioRepository;
        private final HistorialService historialService;

        public HistoryController(
                        UsuarioRepository usuarioRepository,
                        HistorialService historialService) {
                this.usuarioRepository = usuarioRepository;
                this.historialService = historialService;
        }

        @GetMapping("/history")
        public String history(
                        @AuthenticationPrincipal org.springframework.security.core.userdetails.User user,
                        Model model) {

                Usuario usuario = usuarioRepository
                                .findByUsername(user.getUsername())
                                .orElseThrow();

                model.addAttribute("username", user.getUsername());

                model.addAttribute(
                                "historial",
                                historialService.getHistorialUsuario(usuario));

                return "history";
        }

        @PostMapping("/history/clear")
        public ResponseEntity<String> clearHistory() {

                Authentication auth = SecurityContextHolder.getContext().getAuthentication();

                String username = auth.getName();

                historialService.borrarHistorialPorUsername(username);

                return ResponseEntity.ok("OK");
        }
}
package com.example.seniamaps.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.seniamaps.entity.Busqueda;
import com.example.seniamaps.repository.BusquedaRepository;

@Controller
public class HistorialController {

    private final BusquedaRepository busquedaRepository;

    public HistorialController(
            BusquedaRepository busquedaRepository
    ) {
        this.busquedaRepository = busquedaRepository;
    }

    @GetMapping("/history")
    public String history(Model model) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getName().equals("anonymousUser")) {

            return "redirect:/login";
        }

        String username =
                authentication.getName();

        List<Busqueda> historial =
                busquedaRepository
                        .findByUsuarioUsernameOrderByFechaBusquedaDesc(
                                username
                        );
        model.addAttribute(
                "historial",
                historial
        );

        model.addAttribute(
                "username",
                username
        );

        return "history";
    }
}
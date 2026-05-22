package com.example.seniamaps.controller;

import java.time.LocalDateTime;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.example.seniamaps.dto.places.GeoapifyResponseDTO;
import com.example.seniamaps.entity.*;
import com.example.seniamaps.repository.*;
import com.example.seniamaps.services.*;

@RestController
@RequestMapping("/api")
public class PlacesController {

    private final GeoapifyService geoapifyService;
    private final ResultadoService resultadoService;
    private final BusquedaRepository busquedaRepository;
    private final UsuarioRepository usuarioRepository;

    public PlacesController(
            GeoapifyService geoapifyService,
            ResultadoService resultadoService,
            BusquedaRepository busquedaRepository,
            UsuarioRepository usuarioRepository) {

        this.geoapifyService = geoapifyService;
        this.resultadoService = resultadoService;
        this.busquedaRepository = busquedaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/places")
    public GeoapifyResponseDTO searchPlaces(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "3000") int radius,
            @RequestParam(defaultValue = "20") int limit) {

        String username =
                SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow();

        Busqueda busqueda = new Busqueda();
        busqueda.setQuery(keyword.toLowerCase().trim());
        busqueda.setLatitud(lat);
        busqueda.setLongitud(lon);
        busqueda.setFechaBusqueda(LocalDateTime.now());
        busqueda.setUsuario(usuario);

        busqueda = busquedaRepository.save(busqueda);

        GeoapifyResponseDTO response =
                geoapifyService.searchPlaces(lat, lon, keyword, radius, limit);

        resultadoService.saveResults(response.getFeatures(), busqueda);

        return response;
    }
}
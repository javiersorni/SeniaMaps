package com.example.seniamaps.controller;

import java.time.LocalDateTime;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.example.seniamaps.dto.places.GeoapifyResponseDTO;
import com.example.seniamaps.entity.Busqueda;
import com.example.seniamaps.entity.Usuario;
import com.example.seniamaps.repository.BusquedaRepository;
import com.example.seniamaps.repository.UsuarioRepository;
import com.example.seniamaps.services.GeoapifyService;
import com.example.seniamaps.services.ResultadoService;

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
                        @AuthenticationPrincipal org.springframework.security.core.userdetails.User user,
                        @RequestParam double lat,
                        @RequestParam double lon,
                        @RequestParam String keyword,
                        @RequestParam(defaultValue = "3000") int radius,
                        @RequestParam(defaultValue = "20") int limit) {

                String username = user.getUsername();

                Usuario usuario = usuarioRepository.findByUsername(username)
                                .orElseThrow();

                keyword = keyword.toLowerCase().trim();

                Busqueda busqueda = new Busqueda();
                busqueda.setQuery(keyword);
                busqueda.setLatitud(lat);
                busqueda.setLongitud(lon);
                busqueda.setFechaBusqueda(LocalDateTime.now());
                busqueda.setUsuario(usuario);

                busqueda = busquedaRepository.save(busqueda);

                GeoapifyResponseDTO response = geoapifyService.searchPlaces(lat, lon, keyword, radius, limit);

                // 1. Guardar resultados en BD
                resultadoService.saveResults(response.getFeatures(), busqueda);

                // 2. ENRIQUECER ratings del usuario
                resultadoService.enrichRatings(response, usuario);

                // 3. devolver response ya enriquecido
                return response;
        }
}
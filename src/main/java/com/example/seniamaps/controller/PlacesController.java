package com.example.seniamaps.controller;

import java.time.LocalDateTime;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.example.seniamaps.dto.places.GeoapifyResponseDTO;
import com.example.seniamaps.dto.places.PlaceFeatureDTO;
import com.example.seniamaps.dto.places.PlacePropertiesDTO;
import com.example.seniamaps.entity.Busqueda;
import com.example.seniamaps.entity.Resultado;
import com.example.seniamaps.entity.ResultadoBusqueda;
import com.example.seniamaps.entity.Usuario;
import com.example.seniamaps.repository.BusquedaRepository;
import com.example.seniamaps.repository.ResultadoBusquedaRepository;
import com.example.seniamaps.repository.ResultadoRepository;
import com.example.seniamaps.repository.UsuarioRepository;
import com.example.seniamaps.services.GeoapifyService;

@RestController
@RequestMapping("/api")
public class PlacesController {

    private final GeoapifyService geoapifyService;
    private final BusquedaRepository busquedaRepository;
    private final ResultadoRepository resultadoRepository;
    private final ResultadoBusquedaRepository resultadoBusquedaRepository;
    private final UsuarioRepository usuarioRepository;

    public PlacesController(
            GeoapifyService geoapifyService,
            BusquedaRepository busquedaRepository,
            ResultadoRepository resultadoRepository,
            ResultadoBusquedaRepository resultadoBusquedaRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.geoapifyService = geoapifyService;
        this.busquedaRepository = busquedaRepository;
        this.resultadoRepository = resultadoRepository;
        this.resultadoBusquedaRepository = resultadoBusquedaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/places")
    public GeoapifyResponseDTO searchPlaces(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "3000") int radius,
            @RequestParam(defaultValue = "20") int limit
    ) {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        String username = auth.getName();

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow();

        /*
         * =====================================================
         * NORMALIZAR QUERY
         * =====================================================
         */

        keyword = keyword.trim().toLowerCase();

        /*
         * =====================================================
         * SIEMPRE API CALL
         * =====================================================
         */

        System.out.println("🟢 API CALL");

        Busqueda busqueda = new Busqueda();

        busqueda.setQuery(keyword);
        busqueda.setLatitud(lat);
        busqueda.setLongitud(lon);
        busqueda.setFechaBusqueda(LocalDateTime.now());
        busqueda.setUsuario(usuario);

        busqueda = busquedaRepository.save(busqueda);

        GeoapifyResponseDTO response =
                geoapifyService.searchPlaces(
                        lat,
                        lon,
                        keyword,
                        radius,
                        limit
                );

        saveResults(response, busqueda);

        return response;
    }

    /*
     * =====================================================
     * SAVE RESULTS METHOD
     * =====================================================
     */

    private void saveResults(
            GeoapifyResponseDTO response,
            Busqueda busqueda
    ) {

        if (response == null || response.getFeatures() == null) {
            return;
        }

        for (PlaceFeatureDTO feature : response.getFeatures()) {

            PlacePropertiesDTO p = feature.getProperties();

            Resultado resultado =
                    resultadoRepository.findByIdLugar(p.getPlace_id())
                            .orElseGet(() -> {

                                Resultado r = new Resultado();

                                r.setIdLugar(p.getPlace_id());
                                r.setNombre(p.getName());
                                r.setDireccion(p.getFormatted());
                                r.setRating(p.getRating());
                                r.setLatitud(p.getLat());
                                r.setLongitud(p.getLon());
                                r.setTipo("lugar");

                                return resultadoRepository.save(r);
                            });

            ResultadoBusqueda rb = new ResultadoBusqueda();

            rb.setBusqueda(busqueda);
            rb.setResultado(resultado);

            resultadoBusquedaRepository.save(rb);
        }
    }
}
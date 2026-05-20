package com.example.seniamaps.controller;

import org.springframework.web.bind.annotation.*;

import com.example.seniamaps.dto.places.GeoapifyResponseDTO;
import com.example.seniamaps.services.GeoapifyService;

@RestController
@RequestMapping("/api")
public class CoordenadasController {

    private final GeoapifyService geoapifyService;

    public CoordenadasController(
            GeoapifyService geoapifyService) {

        this.geoapifyService = geoapifyService;
    }

    /*
     * =====================================================
     * SEARCH PLACES ONLY
     * =====================================================
     */

    @GetMapping("/search")
    public GeoapifyResponseDTO buscar(

            @RequestParam("lat") double lat,

            @RequestParam("lon") double lon,

            @RequestParam("busqueda") String busqueda
    ) {

        System.out.println(
                "🔎 Búsqueda: "
                        + busqueda
                        + " | Lat: "
                        + lat
                        + " | Lon: "
                        + lon
        );

        return geoapifyService.searchPlaces(
                lat,
                lon,
                busqueda
        );
    }
}
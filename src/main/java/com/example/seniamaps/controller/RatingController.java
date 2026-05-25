package com.example.seniamaps.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.*;

import com.example.seniamaps.entity.Resultado;
import com.example.seniamaps.entity.Usuario;
import com.example.seniamaps.repository.ResultadoRepository;
import com.example.seniamaps.repository.UsuarioRepository;
import com.example.seniamaps.services.ResultadoRatingService;

@RestController
@RequestMapping("/rating")
public class RatingController {

    private final UsuarioRepository usuarioRepository;
    private final ResultadoRepository resultadoRepository;
    private final ResultadoRatingService resultadoRatingService;

    public RatingController(
            UsuarioRepository usuarioRepository,
            ResultadoRepository resultadoRepository,
            ResultadoRatingService resultadoRatingService) {

        this.usuarioRepository = usuarioRepository;
        this.resultadoRepository = resultadoRepository;
        this.resultadoRatingService = resultadoRatingService;
    }

    @PostMapping("/guardar")
    public String guardarRating(
            @RequestParam String idLugar,
            @RequestParam Double rating,
            Principal principal) {

        Usuario usuario = usuarioRepository
                .findByUsername(principal.getName())
                .orElseThrow();

        Resultado resultado = resultadoRepository
                .findByIdLugar(idLugar)
                .orElseThrow();

        resultadoRatingService.saveOrUpdateRating(usuario, resultado, rating);

        return "OK";
    }
}
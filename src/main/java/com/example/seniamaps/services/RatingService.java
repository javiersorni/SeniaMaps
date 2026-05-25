package com.example.seniamaps.services;

import org.springframework.stereotype.Service;

import com.example.seniamaps.entity.*;
import com.example.seniamaps.repository.ResultadoRatingRepository;

@Service
public class RatingService {

    private final ResultadoRatingRepository repository;
    private final ResultadoRatingRepository resultadoRatingRepository;

    public RatingService(ResultadoRatingRepository repository, ResultadoRatingRepository resultadoRatingRepository) {
        this.repository = repository;
        this.resultadoRatingRepository = resultadoRatingRepository;
    }

    public void saveOrUpdateRating(Usuario usuario, Resultado resultado, Double rating) {

        ResultadoRating rr = repository
                .findByUsuarioAndResultado(usuario, resultado)
                .orElse(new ResultadoRating());

        rr.setUsuario(usuario);
        rr.setResultado(resultado);
        rr.setRating(rating);

        repository.save(rr);
    }

    public Double getUserRating(Usuario usuario, Resultado resultado) {
        return resultadoRatingRepository
                .findByUsuarioAndResultado(usuario, resultado)
                .map(ResultadoRating::getRating)
                .orElse(null);
    }
}
package com.example.seniamaps.services;

import org.springframework.stereotype.Service;

import com.example.seniamaps.entity.Resultado;
import com.example.seniamaps.entity.ResultadoRating;
import com.example.seniamaps.entity.Usuario;
import com.example.seniamaps.repository.ResultadoRatingRepository;

@Service
public class ResultadoRatingService {

    private final ResultadoRatingRepository resultadoRatingRepository;

    public ResultadoRatingService(ResultadoRatingRepository resultadoRatingRepository) {
        this.resultadoRatingRepository = resultadoRatingRepository;
    }

    public void saveOrUpdateRating(Usuario usuario, Resultado resultado, Double rating) {

        ResultadoRating rr = resultadoRatingRepository
            .findByUsuarioAndResultado(usuario, resultado)
            .orElse(new ResultadoRating());

        rr.setUsuario(usuario);
        rr.setResultado(resultado);
        rr.setRating(rating);

        resultadoRatingRepository.save(rr);
    }

    public Double getUserRating(Usuario usuario, Resultado resultado) {

        return resultadoRatingRepository
            .findByUsuarioAndResultado(usuario, resultado)
            .map(ResultadoRating::getRating)
            .orElse(null);
    }
}
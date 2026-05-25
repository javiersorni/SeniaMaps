package com.example.seniamaps.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.seniamaps.entity.ResultadoRating;
import com.example.seniamaps.entity.Usuario;
import com.example.seniamaps.entity.Resultado;

public interface ResultadoRatingRepository extends JpaRepository<ResultadoRating, Long> {

    Optional<ResultadoRating> findByUsuarioAndResultado(Usuario usuario, Resultado resultado);
}
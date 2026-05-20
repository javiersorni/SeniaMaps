package com.example.seniamaps.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.seniamaps.entity.Resultado;

public interface ResultadoRepository
        extends JpaRepository<Resultado, Long> {

    Optional<Resultado>
    findByIdLugar(String idLugar);
}
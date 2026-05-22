package com.example.seniamaps.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.seniamaps.entity.Busqueda;
import com.example.seniamaps.entity.Resultado;
import com.example.seniamaps.entity.ResultadoBusqueda;

public interface ResultadoBusquedaRepository
        extends JpaRepository<ResultadoBusqueda, Long> {

    List<ResultadoBusqueda> findByBusqueda(Busqueda busqueda);

    boolean existsByBusquedaAndResultado(Busqueda busqueda, Resultado resultado);
}
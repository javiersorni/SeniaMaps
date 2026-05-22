package com.example.seniamaps.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.seniamaps.entity.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByNombreCategoria(String nombreCategoria);
}
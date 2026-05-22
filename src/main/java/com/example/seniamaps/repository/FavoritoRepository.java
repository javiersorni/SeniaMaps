package com.example.seniamaps.repository;

import com.example.seniamaps.entity.Favorito;
import com.example.seniamaps.entity.Resultado;
import com.example.seniamaps.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, Long> {
    // Busca todos los favoritos asociados a la entidad de un usuario concreto
    List<Favorito> findByUsuario(Usuario usuario);

    boolean existsByUsuarioAndResultado(Usuario usuario, Resultado resultado);
}
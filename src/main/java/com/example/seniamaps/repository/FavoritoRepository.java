package com.example.seniamaps.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.seniamaps.entity.Favorito;
import com.example.seniamaps.entity.Resultado;
import com.example.seniamaps.entity.Usuario;

public interface FavoritoRepository extends JpaRepository<Favorito, Long> {

    List<Favorito> findByUsuario(Usuario usuario);

    boolean existsByUsuarioAndResultado(Usuario usuario, Resultado resultado);

    Optional<Favorito> findByUsuarioAndResultado(Usuario usuario, Resultado resultado);

    boolean existsByUsuarioAndResultadoIdLugar(Usuario usuario, String idLugar);
}
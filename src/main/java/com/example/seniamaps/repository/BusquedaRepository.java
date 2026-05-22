package com.example.seniamaps.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.seniamaps.entity.Busqueda;
import com.example.seniamaps.entity.Usuario;

public interface BusquedaRepository
                extends JpaRepository<Busqueda, Long> {

        Optional<Busqueda> findTopByQueryAndFechaBusquedaAfterOrderByFechaBusquedaDesc(
                        String query,
                        LocalDateTime fecha);

        List<Busqueda> findByUsuarioUsernameOrderByFechaBusquedaDesc(
                        String username);

        List<Busqueda> findByUsuario(Usuario usuario);
}
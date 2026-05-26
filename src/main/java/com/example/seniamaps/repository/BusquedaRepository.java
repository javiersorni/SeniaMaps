package com.example.seniamaps.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.seniamaps.entity.Busqueda;
import com.example.seniamaps.entity.Usuario;

public interface BusquedaRepository
                extends JpaRepository<Busqueda, Long> {

        Optional<Busqueda> findTopByQueryAndFechaBusquedaAfterOrderByFechaBusquedaDesc(
                        String query,
                        LocalDateTime fecha);

        List<Busqueda> findByUsuarioUsernameOrderByFechaBusquedaDesc(
                        String username);

        @EntityGraph(attributePaths = {
                        "resultadosBusqueda",
                        "resultadosBusqueda.resultado",
                        "resultadosBusqueda.resultado.categorias"
        })
        List<Busqueda> findByUsuario(Usuario usuario);

        @Modifying
        @Query("DELETE FROM Busqueda b WHERE b.usuario.username = :username")
        void deleteByUsername(@Param("username") String username);
}
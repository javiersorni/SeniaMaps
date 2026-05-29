package com.example.seniamaps.repository;

import com.example.seniamaps.entity.Etiqueta;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EtiquetaRepository extends JpaRepository<Etiqueta, Long> {

    // Filter tags for their exact name.
    List<Etiqueta> findByNombreEtiqueta(String nombreEtiqueta);

    // Delete a tag matching their result.
    void deleteByResultadoIdResultadoAndNombreEtiqueta(Long idResultado, String nombreEtiqueta);

    // Delete all tags when removing favourite.
    void deleteByResultadoIdResultado(Long idResultado);

    @EntityGraph(attributePaths = { "resultado" })
    List<Etiqueta> findAll();

    @Query("SELECT e FROM Etiqueta e JOIN FETCH e.resultado")
    List<Etiqueta> findAllWithResultado();
}
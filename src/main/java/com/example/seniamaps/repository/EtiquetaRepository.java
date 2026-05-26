package com.example.seniamaps.repository;

import com.example.seniamaps.entity.Etiqueta;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EtiquetaRepository extends JpaRepository<Etiqueta, Long> {

    // Filtra las etiquetas por su nombre exacto
    List<Etiqueta> findByNombreEtiqueta(String nombreEtiqueta);

    // Elimina una etiqueta concreta vinculada a un resultado específico
    void deleteByResultadoIdResultadoAndNombreEtiqueta(Long idResultado, String nombreEtiqueta);

    // Elimina todas las etiquetas de un resultado (Para cuando se quita de
    // favoritos)
    void deleteByResultadoIdResultado(Long idResultado);

    @EntityGraph(attributePaths = { "resultado" })
    List<Etiqueta> findAll();

    @Query("SELECT e FROM Etiqueta e JOIN FETCH e.resultado")
    List<Etiqueta> findAllWithResultado();
}
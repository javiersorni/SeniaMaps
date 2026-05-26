package com.example.seniamaps.mapper;

import org.springframework.stereotype.Component;

import com.example.seniamaps.dto.ResultadoDTO;
import com.example.seniamaps.entity.Busqueda;
import com.example.seniamaps.entity.Categoria;
import com.example.seniamaps.entity.Resultado;
import com.example.seniamaps.entity.ResultadoBusqueda;
import com.example.seniamaps.entity.Usuario;
import com.example.seniamaps.services.ResultadoRatingService;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;

@Component
public class HistorialMapper {

    private final ResultadoRatingService ratingService;
    private final CategoriaMapper categoriaMapper;

    public HistorialMapper(ResultadoRatingService ratingService, CategoriaMapper categoriaMapper) {
        this.ratingService = ratingService;
        this.categoriaMapper = categoriaMapper;
    }

    public List<ResultadoDTO> toResultadosDTO(Busqueda busqueda, Usuario usuario) {

        return busqueda.getResultadosBusqueda()
                .stream()
                .map(rb -> mapResultado(rb, usuario))
                .toList();
    }

    private ResultadoDTO mapResultado(ResultadoBusqueda rb, Usuario usuario) {

        Resultado r = rb.getResultado();

        ResultadoDTO dto = new ResultadoDTO();

        // ✅ ID del historial (RELACIÓN)
        dto.setIdResultado(rb.getId());

        // ✅ ID del lugar
        dto.setIdLugar(r.getIdLugar());

        dto.setNombre(r.getNombre());
        dto.setDireccion(r.getDireccion());

        dto.setLatitud(r.getLatitud());
        dto.setLongitud(r.getLongitud());

        dto.setCategorias(
                r.getCategorias() == null
                        ? List.of()
                        : r.getCategorias().stream()
                                .map(Categoria::getNombreCategoria)
                                .filter(Objects::nonNull)
                                .filter(s -> !s.isBlank())
                                .distinct()
                                .collect(Collectors.toCollection(ArrayList::new)));

        dto.setUserRating(ratingService.getUserRating(usuario, r));
        dto.setFechaConsulta(rb.getFechaConsulta());

        return dto;
    }
}
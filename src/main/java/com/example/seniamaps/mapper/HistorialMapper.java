package com.example.seniamaps.mapper;

import org.springframework.stereotype.Component;

import com.example.seniamaps.dto.BusquedaDTO;
import com.example.seniamaps.dto.ResultadoDTO;
import com.example.seniamaps.entity.Busqueda;
import com.example.seniamaps.entity.Categoria;
import com.example.seniamaps.entity.Resultado;
import com.example.seniamaps.entity.ResultadoBusqueda;

import java.util.stream.Collectors;

@Component
public class HistorialMapper {

    public BusquedaDTO toDTO(Busqueda b) {

        BusquedaDTO dto = new BusquedaDTO();

        dto.setIdBusqueda(b.getIdBusqueda());
        dto.setQuery(b.getQuery());
        dto.setLatitud(b.getLatitud());
        dto.setLongitud(b.getLongitud());
        dto.setFechaBusqueda(b.getFechaBusqueda());

        dto.setResultados(
                b.getResultadosBusqueda()
                        .stream()
                        .map(this::mapResultado)
                        .collect(Collectors.toList())
        );

        return dto;
    }

    private ResultadoDTO mapResultado(ResultadoBusqueda rb) {

        Resultado r = rb.getResultado();

        ResultadoDTO dto = new ResultadoDTO();

        dto.setIdLugar(r.getIdLugar());
        dto.setNombre(r.getNombre());
        dto.setDireccion(r.getDireccion());
        dto.setRating(r.getRating());
        dto.setLatitud(r.getLatitud());
        dto.setLongitud(r.getLongitud());

        // categorías limpias (IMPORTANTE)
        if (r.getCategorias() != null) {
            dto.setCategorias(
                    r.getCategorias()
                            .stream()
                            .map(Categoria::getNombreCategoria)
                            .toList()
            );
        }

        // historial
        dto.setFechaConsulta(rb.getFechaConsulta());

        return dto;
    }
}
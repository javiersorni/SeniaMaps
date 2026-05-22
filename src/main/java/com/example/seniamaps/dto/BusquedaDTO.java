package com.example.seniamaps.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class BusquedaDTO {

    private Long idBusqueda;
    private String query;
    private Double latitud;
    private Double longitud;
    private LocalDateTime fechaBusqueda;

    private List<ResultadoDTO> resultados;


}
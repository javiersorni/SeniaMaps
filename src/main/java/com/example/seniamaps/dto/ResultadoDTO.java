package com.example.seniamaps.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class ResultadoDTO {

    private Long idResultado;

    private String idLugar;
    private String nombre;
    private String direccion;

    private Double latitud;
    private Double longitud;

    private Double userRating;

    private List<String> categorias;

    private List<String> etiquetas;

    private LocalDateTime fechaConsulta;
}
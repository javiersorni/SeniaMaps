package com.example.seniamaps.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class ResultadoDTO {

    private String idLugar;
    private String nombre;
    private String direccion;
    private Double rating;
    private Double latitud;
    private Double longitud;

    private List<String> categorias;

    private LocalDateTime fechaConsulta;

}
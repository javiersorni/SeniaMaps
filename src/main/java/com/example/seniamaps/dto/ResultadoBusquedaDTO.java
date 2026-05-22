package com.example.seniamaps.dto;

import java.util.List;

import lombok.Data;

@Data
public class ResultadoBusquedaDTO {

    private String nombre;
    private String direccion;
    private Double rating;
    private String idLugar;

    private List<String> categorias;
}
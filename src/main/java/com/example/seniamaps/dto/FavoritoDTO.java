package com.example.seniamaps.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FavoritoDTO {

    private Long idFavorito;   // 👈 ESTE ES EL ID REAL BD

    private ResultadoDTO resultado;
}
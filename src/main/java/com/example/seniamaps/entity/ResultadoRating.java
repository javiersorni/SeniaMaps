package com.example.seniamaps.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "resultado_rating",
       uniqueConstraints = @UniqueConstraint(columnNames = {"idUsuario", "idResultado"}))
@Getter
@Setter
public class ResultadoRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idResultadoRating;

    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "idResultado", nullable = false)
    private Resultado resultado;

    private Double rating;
}
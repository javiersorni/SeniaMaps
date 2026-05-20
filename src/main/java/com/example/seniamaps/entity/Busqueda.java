package com.example.seniamaps.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "busqueda")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Busqueda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBusqueda;

    private String query;

    private Double latitud;

    private Double longitud;

    private LocalDateTime fechaBusqueda;

    @ManyToOne
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;
}
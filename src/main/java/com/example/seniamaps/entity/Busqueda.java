package com.example.seniamaps.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "busqueda")
@Getter
@Setter
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

    @OneToMany(mappedBy = "busqueda", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<ResultadoBusqueda> resultadosBusqueda = new ArrayList<>();
}
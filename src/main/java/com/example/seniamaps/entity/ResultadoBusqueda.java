package com.example.seniamaps.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "resultado_busqueda")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultadoBusqueda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idBusqueda")
    private Busqueda busqueda;

    @ManyToOne
    @JoinColumn(name = "idResultado")
    private Resultado resultado;

    @Column(name = "fechaConsulta")
    private LocalDateTime fechaConsulta;
}
package com.example.seniamaps.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

//Entity made for DB Result_Search

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idResultado")
    private Resultado resultado;

    @Column(name = "fechaConsulta")
    private LocalDateTime fechaConsulta;
}
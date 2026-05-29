package com.example.seniamaps.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Entity made for DB Tags

@Entity
@Table(name="etiqueta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Etiqueta {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="idEtiqueta")
    private Long idEtiqueta;
    
    @Column(name="nombreEtiqueta", nullable=false)
    private String nombreEtiqueta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idResultado", nullable = false)
    private Resultado resultado;
    
}
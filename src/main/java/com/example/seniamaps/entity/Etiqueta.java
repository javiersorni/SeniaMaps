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

@Entity
@Table(name="etiqueta")
public class Etiqueta {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="idEtiqueta")
    private Long idEtiqueta;
    
    @Column(name="nombreEtiqueta")
    private String nombreEtiqueta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idResultado", nullable = false)
    private Resultado resultado;

    public Long getIdEtiqueta() {
        return idEtiqueta;
    }

    public String getNombreEtiqueta() {
        return nombreEtiqueta;
    }

    public void setNombreEtiqueta(String nombreEtiqueta) {
        this.nombreEtiqueta = nombreEtiqueta;
    }
    
}
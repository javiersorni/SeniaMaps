package com.example.seniamaps.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.*;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "resultado")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Resultado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idResultado;

    private String idLugar;

    private String nombre;

    private String direccion;

    private Double latitud;

    private Double longitud;

    @ManyToMany
    private Set<Categoria> categorias = new HashSet<>();
    @OneToMany(mappedBy = "resultado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Etiqueta> etiquetas = new ArrayList<>();
}
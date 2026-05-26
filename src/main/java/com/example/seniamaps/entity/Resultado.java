package com.example.seniamaps.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
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
    @JoinTable(name = "resultado_categoria", joinColumns = @JoinColumn(name = "idResultado"), inverseJoinColumns = @JoinColumn(name = "idCategoria"))
    private List<Categoria> categorias = new ArrayList<>();
    @OneToMany(mappedBy = "resultado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Etiqueta> etiquetas = new ArrayList<>();
}
package com.example.seniamaps.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.seniamaps.entity.Etiqueta;
import com.example.seniamaps.repository.EtiquetaRepository;
import com.example.seniamaps.repository.ResultadoRepository;

import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/etiquetas")
public class EtiquetasController {

    @Autowired
    private EtiquetaRepository etiquetaRepository;
    
    @Autowired
    private ResultadoRepository resultadoRepository; // El repositorio de vuestros favoritos

    // 1. Vista principal y Filtrado por etiqueta
    @GetMapping
    public String gestionarEtiquetas(@RequestParam(required = false) String tag, Model model) {
        List<Etiqueta> etiquetasObtenidas;
        
        if (tag != null && !tag.trim().isEmpty()) {
            // Filtramos la tabla intermedia por el nombre de la etiqueta
            etiquetasObtenidas = etiquetaRepository.findByNombreEtiqueta(tag);
            model.addAttribute("tagSeleccionada", tag);
        } else {
            // Si no hay filtro, traemos todas las relaciones existentes
            etiquetasObtenidas = etiquetaRepository.findAll();
        }
        
        model.addAttribute("etiquetas", etiquetasObtenidas);
        return "etiquetas";
    }

    // 2. Eliminar una etiqueta concreta de un resultado específico
    @Transactional // Obligatorio para operaciones de borrado personalizadas en JPA
    @PostMapping("/delete-etiqueta")
    public String eliminarEtiquetaDeFavorito(@RequestParam Long resultadoId, @RequestParam String tagName) {
        etiquetaRepository.deleteByResultadoIdResultadoAndNombreEtiqueta(resultadoId, tagName);
        return "redirect:/etiquetas";
    }

    // 3. Eliminar un favorito (Resultado) y quitarle todas sus etiquetas automáticamente
    @Transactional
    @PostMapping("/delete-favorito")
    public String eliminarFavoritoCompleto(@RequestParam Long resultadoId) {
        // Requisito: Primero borramos todas las etiquetas asociadas a este idResultado
        etiquetaRepository.deleteByResultadoIdResultado(resultadoId);
        
        // Ahora ya podemos borrar de forma segura el favorito de su tabla sin violar claves foráneas
        resultadoRepository.deleteById(resultadoId);
        
        return "redirect:/etiquetas";
    }
}
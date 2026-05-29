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
    private ResultadoRepository resultadoRepository;

    //tag filter
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

    //delete tag
    @Transactional // Obligatorio para operaciones de borrado personalizadas en JPA
    @PostMapping("/delete-etiqueta")
    public String eliminarEtiquetaDeFavorito(@RequestParam Long resultadoId, @RequestParam String tagName) {
        etiquetaRepository.deleteByResultadoIdResultadoAndNombreEtiqueta(resultadoId, tagName);
        return "redirect:/etiquetas";
    }

    //delete a favourite result
    @Transactional
    @PostMapping("/delete-favorito")
    public String eliminarFavoritoCompleto(@RequestParam Long resultadoId) {
        //delete every tag
        etiquetaRepository.deleteByResultadoIdResultado(resultadoId);
        
        //delete the favourite
        resultadoRepository.deleteById(resultadoId);
        
        return "redirect:/etiquetas";
    }
}
package com.example.seniamaps.controller;

import com.example.seniamaps.entity.*;
import com.example.seniamaps.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/favoritos")
public class FavoritosController {

    @Autowired
    private FavoritoRepository favoritoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ResultadoRepository resultadoRepository;

    @Autowired
    private EtiquetaRepository etiquetaRepository; // Inyectamos el nuevo repositorio

    // =========================
    // LISTAR FAVORITOS
    // =========================
    @GetMapping
    public String verFavoritos(Model model,
                               @AuthenticationPrincipal UserDetails userDetails) {

        Usuario usuario = usuarioRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Favorito> listaFavoritos = favoritoRepository.findByUsuario(usuario);

        model.addAttribute("username", userDetails.getUsername());
        model.addAttribute("listaFavoritos", listaFavoritos); // Mantenemos vuestro nombre original

        return "favoritos";
    }

    // =========================
    // CREAR ETIQUETA DESDE FAVORITOS
    // =========================
    @PostMapping("/etiqueta/add")
    public String añadirEtiqueta(
            @RequestParam Long idResultado,
            @RequestParam String nombreEtiqueta,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        
        try {
            if (nombreEtiqueta == null || nombreEtiqueta.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("errorEtiqueta", "El nombre de la etiqueta no puede estar vacío.");
                return "redirect:/favoritos";
            }

            Usuario usuario = usuarioRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            Resultado resultado = resultadoRepository.findById(idResultado)
                    .orElseThrow(() -> new RuntimeException("Resultado no encontrado"));

            Etiqueta etiqueta = new Etiqueta();
            etiqueta.setNombreEtiqueta(nombreEtiqueta.trim().toLowerCase()); // Guardado estético en minúsculas
            etiqueta.setUsuario(usuario);
            etiqueta.setResultado(resultado);

            etiquetaRepository.save(etiqueta);
            redirectAttributes.addFlashAttribute("exitoEtiqueta", "¡Etiqueta añadida con éxito!");

        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorEtiqueta", "Ya has añadido esa etiqueta a este sitio.");
        }

        return "redirect:/favoritos";
    }

    // =========================
    // ELIMINAR FAVORITO
    // =========================
    @PostMapping("/eliminar")
    public String quitarFavorito(@RequestParam Long idFavorito,
                                 @AuthenticationPrincipal UserDetails userDetails) {

        Favorito favorito = favoritoRepository.findById(idFavorito)
                .orElseThrow(() -> new RuntimeException("Favorito no encontrado"));

        if (!favorito.getUsuario().getUsername().equals(userDetails.getUsername())) {
            return "redirect:/favoritos?error=denied";
        }

        favoritoRepository.delete(favorito);

        return "redirect:/favoritos?success=deleted";
    }

    // =========================
    // AGREGAR FAVORITO (CLEAN API)
    // =========================
    @PostMapping("/agregar")
    @ResponseBody
    public String agregarFavorito(@RequestParam String idLugar,
                                  @AuthenticationPrincipal UserDetails userDetails) {

        Usuario usuario = usuarioRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Resultado resultado = resultadoRepository.findByIdLugar(idLugar)
                .orElseThrow(() -> new RuntimeException("Resultado no encontrado"));

        boolean yaExiste = favoritoRepository.existsByUsuarioAndResultado(usuario, resultado);

        if (yaExiste) {
            return "YA_EXISTE";
        }

        Favorito favorito = new Favorito();
        favorito.setUsuario(usuario);
        favorito.setResultado(resultado);

        favoritoRepository.save(favorito);

        return "OK";
    }
    
}
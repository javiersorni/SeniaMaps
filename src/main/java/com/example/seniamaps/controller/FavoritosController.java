package com.example.seniamaps.controller;

import com.example.seniamaps.entity.*;
import com.example.seniamaps.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        model.addAttribute("listaFavoritos", listaFavoritos);

        return "favoritos";
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

        // 1. Usuario
        Usuario usuario = usuarioRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2. Resultado (SOURCE OF TRUTH)
        Resultado resultado = resultadoRepository.findByIdLugar(idLugar)
                .orElseThrow(() -> new RuntimeException("Resultado no encontrado"));

        // 3. Evitar duplicados
        boolean yaExiste = favoritoRepository.existsByUsuarioAndResultado(usuario, resultado);

        if (yaExiste) {
            return "YA_EXISTE";
        }

        // 4. Crear favorito
        Favorito favorito = new Favorito();
        favorito.setUsuario(usuario);
        favorito.setResultado(resultado);

        favoritoRepository.save(favorito);

        return "OK";
    }
}
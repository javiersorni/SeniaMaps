package com.example.seniamaps.controller;

import com.example.seniamaps.dto.FavoritoDTO;
import com.example.seniamaps.entity.*;
import com.example.seniamaps.mapper.FavoritoMapper;
import com.example.seniamaps.repository.*;
import com.example.seniamaps.services.ResultadoRatingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal; // <-- Importación crucial
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
        private EtiquetaRepository etiquetaRepository;

        @Autowired
        private FavoritoMapper favoritoMapper;

        @Autowired
        private ResultadoRatingService resultadoRatingService;

        // =========================
        // LISTAR FAVORITOS
        // =========================
        @GetMapping
        public String verFavoritos(Model model, Principal principal) {

                if (principal == null)
                        return "redirect:/login";

                Usuario usuario = usuarioRepository.findByUsername(principal.getName())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                                "Usuario no encontrado"));

                List<FavoritoDTO> listaFavoritos = favoritoRepository.findByUsuario(usuario)
                                .stream()
                                .map(f -> favoritoMapper.toDTO(f, usuario))
                                .toList();

                model.addAttribute("username", principal.getName());
                model.addAttribute("listaFavoritos", listaFavoritos);

                return "favoritos";
        }

        // =========================
        // CREAR ETIQUETA
        // =========================
        @PostMapping("/etiqueta/add")
        public String añadirEtiqueta(
                        @RequestParam Long idResultado,
                        @RequestParam String nombreEtiqueta,
                        Principal principal,
                        RedirectAttributes redirectAttributes) {

                if (principal == null)
                        return "redirect:/login";

                try {
                        if (nombreEtiqueta == null || nombreEtiqueta.trim().isEmpty()) {
                                redirectAttributes.addFlashAttribute(
                                                "errorEtiqueta",
                                                "El nombre de la etiqueta no puede estar vacío.");
                                return "redirect:/favoritos";
                        }

                        Usuario usuario = usuarioRepository.findByUsername(principal.getName())
                                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                        Resultado resultado = resultadoRepository.findById(idResultado)
                                        .orElseThrow(() -> new RuntimeException("Resultado no encontrado"));

                        Etiqueta etiqueta = new Etiqueta();
                        etiqueta.setNombreEtiqueta(nombreEtiqueta.trim().toLowerCase());
                        etiqueta.setUsuario(usuario);
                        etiqueta.setResultado(resultado);

                        etiquetaRepository.save(etiqueta);

                        redirectAttributes.addFlashAttribute(
                                        "exitoEtiqueta",
                                        "¡Etiqueta añadida con éxito!");

                } catch (DataIntegrityViolationException e) {
                        redirectAttributes.addFlashAttribute(
                                        "errorEtiqueta",
                                        "Ya has añadido esa etiqueta a este sitio.");
                }

                return "redirect:/favoritos";
        }

        // =========================
        // ELIMINAR FAVORITO
        // =========================
        @PostMapping("/eliminar")
        public String quitarFavorito(
                        @RequestParam Long idFavorito,
                        Principal principal) {

                if (principal == null)
                        return "redirect:/login";

                Favorito favorito = favoritoRepository.findById(idFavorito)
                                .orElseThrow(() -> new RuntimeException("Favorito no encontrado"));

                if (!favorito.getUsuario().getUsername().equals(principal.getName())) {
                        return "redirect:/favoritos?error=denied";
                }

                favoritoRepository.delete(favorito);

                return "redirect:/favoritos?success=deleted";
        }

        // =========================
        // AGREGAR FAVORITO
        // =========================
        @PostMapping("/agregar")
        @ResponseBody
        public String agregarFavorito(
                        @RequestParam String idLugar,
                        Principal principal) {

                if (principal == null)
                        return "USUARIO_NO_AUTENTICADO";

                Usuario usuario = usuarioRepository.findByUsername(principal.getName())
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                Resultado resultado = resultadoRepository.findByIdLugar(idLugar)
                                .orElseThrow(() -> new RuntimeException("Resultado no encontrado"));

                if (favoritoRepository.existsByUsuarioAndResultado(usuario, resultado)) {
                        return "YA_EXISTE";
                }

                Favorito favorito = new Favorito();
                favorito.setUsuario(usuario);
                favorito.setResultado(resultado);

                favoritoRepository.save(favorito);

                return "OK";
        }

        @PostMapping("/rating/update")
        public String updateRating(
                        @RequestParam Long idResultado,
                        @RequestParam Double rating,
                        Principal principal) {

                if (principal == null)
                        return "redirect:/login";

                Usuario usuario = usuarioRepository.findByUsername(principal.getName())
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                Resultado resultado = resultadoRepository.findById(idResultado)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resultado no encontrado"));

                resultadoRatingService.saveOrUpdateRating(usuario, resultado, rating);

                return "redirect:/favoritos";
        }
}
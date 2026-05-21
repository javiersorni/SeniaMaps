package com.example.seniamaps.controller;

import com.example.seniamaps.entity.Favorito;
import com.example.seniamaps.entity.Usuario;
import com.example.seniamaps.repository.FavoritoRepository;
import com.example.seniamaps.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/favoritos")
public class FavoritosController {

    @Autowired
    private FavoritoRepository favoritoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Mostrar los favoritos del usuario autenticado
    @GetMapping
    public String verFavoritos(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        
        // Buscamos al usuario completo en la base de datos para obtener su idUsuario
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Recuperamos solo los favoritos pertenecientes a este usuario
        List<Favorito> listaFavoritos = favoritoRepository.findByUsuario(usuario);

        model.addAttribute("username", username);
        model.addAttribute("listaFavoritos", listaFavoritos);
        
        return "favoritos";
    }

    // Quitar un elemento de favoritos
    @PostMapping("/eliminar")
    public String quitarFavorito(@RequestParam("idFavorito") Long idFavorito, 
                                 @AuthenticationPrincipal UserDetails userDetails) {
        
        Favorito favorito = favoritoRepository.findById(idFavorito)
                .orElseThrow(() -> new IllegalArgumentException("El favorito no existe"));

        // Seguridad elemental: Validar que el favorito de verdad pertenece al usuario que ha iniciado sesión
        if (!favorito.getUsuario().getUsername().equals(userDetails.getUsername())) {
            return "redirect:/favoritos?error=denied";
        }

        favoritoRepository.delete(favorito);

        return "redirect:/favoritos?success=deleted";
    }
}
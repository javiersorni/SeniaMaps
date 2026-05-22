package com.example.seniamaps.controller;

import com.example.seniamaps.entity.Categoria;
import com.example.seniamaps.entity.Favorito;
import com.example.seniamaps.entity.Resultado;
import com.example.seniamaps.entity.Usuario;
import com.example.seniamaps.repository.CategoriaRepository;
import com.example.seniamaps.repository.FavoritoRepository;
import com.example.seniamaps.repository.ResultadoRepository;
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
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
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
    private CategoriaRepository categoriaRepository;

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

        // Seguridad elemental: Validar que el favorito de verdad pertenece al usuario
        // que ha iniciado sesión
        if (!favorito.getUsuario().getUsername().equals(userDetails.getUsername())) {
            return "redirect:/favoritos?error=denied";
        }

        favoritoRepository.delete(favorito);

        return "redirect:/favoritos?success=deleted";
    }

    @PostMapping("/agregar")
    @ResponseBody
    public String agregarFavorito(
            @RequestParam String idLugar,
            @RequestParam String nombre,
            @RequestParam String direccion,
            @RequestParam Double latitud,
            @RequestParam Double longitud,
            @RequestParam(required = false) Double rating,
            @RequestParam(required = false) List<String> categories,
            @AuthenticationPrincipal UserDetails userDetails) {

        Usuario usuario = usuarioRepository
                .findByUsername(userDetails.getUsername())
                .orElseThrow();

        // 1. Buscar o crear Resultado
        Resultado resultado = resultadoRepository
                .findByIdLugar(idLugar)
                .orElseGet(() -> {

                    Resultado nuevo = new Resultado();
                    nuevo.setIdLugar(idLugar);
                    nuevo.setNombre(nombre);
                    nuevo.setDireccion(direccion);
                    nuevo.setLatitud(latitud);
                    nuevo.setLongitud(longitud);
                    nuevo.setRating(rating);

                    return nuevo;
                });

        // 2. ASIGNAR CATEGORÍAS (NUEVO SISTEMA)
        if (categories != null && !categories.isEmpty()) {

            List<Categoria> listaCategorias = new ArrayList<>();

            for (String catName : categories) {

                Categoria categoria = categoriaRepository
                        .findByNombreCategoria(catName)
                        .orElseGet(() -> {
                            Categoria c = new Categoria();
                            c.setNombreCategoria(catName);
                            return categoriaRepository.save(c);
                        });

                listaCategorias.add(categoria);
            }

            resultado.setCategorias(listaCategorias);
        }

        // 3. Guardar resultado (IMPORTANTE: save siempre aquí)
        resultado = resultadoRepository.save(resultado);

        // 4. Evitar duplicados en favoritos
        boolean yaExiste = favoritoRepository
                .existsByUsuarioAndResultado(usuario, resultado);

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
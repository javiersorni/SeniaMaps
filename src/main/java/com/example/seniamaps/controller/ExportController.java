package com.example.seniamaps.controller;

import com.example.seniamaps.dto.FavoritoDTO;
import com.example.seniamaps.entity.Usuario;
import com.example.seniamaps.repository.UsuarioRepository;
import com.example.seniamaps.services.FavoritosService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ExportController {

    private final FavoritosService favoritosService;
    private final UsuarioRepository usuarioRepository;

    @GetMapping("/export/csv")
    public void exportCsv(HttpServletResponse response) throws IOException {

        org.springframework.security.core.userdetails.User userDetails =
                (org.springframework.security.core.userdetails.User)
                        SecurityContextHolder.getContext()
                                .getAuthentication()
                                .getPrincipal();

        Usuario usuario = usuarioRepository
                .findByUsername(userDetails.getUsername())
                .orElseThrow();

        List<FavoritoDTO> favoritos =
                favoritosService.getFavoritos(usuario);

        //CONFIG CSV
        response.setContentType("text/csv");
        response.setCharacterEncoding("UTF-8");

        response.setHeader(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=favoritos.csv");

        PrintWriter writer = response.getWriter();

        writer.println(
                "ID,Nombre,Direccion,Latitud,Longitud,Rating,Categorias,Etiquetas");

        for (FavoritoDTO fav : favoritos) {

            var dto = fav.getResultado();

            writer.println(
                    dto.getIdResultado() + "," +
                    clean(dto.getNombre()) + "," +
                    clean(dto.getDireccion()) + "," +
                    dto.getLatitud() + "," +
                    dto.getLongitud() + "," +
                    dto.getUserRating() + "," +
                    cleanList(dto.getCategorias()) + "," +
                    cleanList(dto.getEtiquetas()));
        }

        writer.flush();
        writer.close();
    }

    private String clean(String value) {

        if (value == null)
            return "";

        return value.replace(",", " ")
                .replace("\n", " ")
                .replace("\r", " ");
    }

    private String cleanList(List<String> list) {

        if (list == null || list.isEmpty())
            return "";

        return String.join(" | ", list);
    }
}
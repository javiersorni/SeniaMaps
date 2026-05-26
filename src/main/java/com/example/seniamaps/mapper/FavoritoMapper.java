package com.example.seniamaps.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.seniamaps.dto.FavoritoDTO;
import com.example.seniamaps.dto.ResultadoDTO;
import com.example.seniamaps.entity.Categoria;
import com.example.seniamaps.entity.Etiqueta;
import com.example.seniamaps.entity.Favorito;
import com.example.seniamaps.entity.Resultado;
import com.example.seniamaps.entity.Usuario;
import com.example.seniamaps.services.ResultadoRatingService;

@Component
public class FavoritoMapper {

    private final ResultadoRatingService ratingService;

    public FavoritoMapper(ResultadoRatingService ratingService) {
        this.ratingService = ratingService;
    }

    public FavoritoDTO toDTO(Favorito favorito, Usuario usuario) {

        Resultado r = favorito.getResultado();

        // DTO del resultado
        ResultadoDTO resultadoDTO = new ResultadoDTO();

        resultadoDTO.setIdResultado(r.getIdResultado());
        resultadoDTO.setIdLugar(r.getIdLugar());
        resultadoDTO.setNombre(r.getNombre());
        resultadoDTO.setDireccion(r.getDireccion());
        resultadoDTO.setLatitud(r.getLatitud());
        resultadoDTO.setLongitud(r.getLongitud());

        resultadoDTO.setUserRating(
                ratingService.getUserRating(usuario, r)
        );

        if (r.getCategorias() != null) {
            resultadoDTO.setCategorias(
                    r.getCategorias()
                            .stream()
                            .map(Categoria::getNombreCategoria)
                            .toList()
            );
        }

        if (r.getEtiquetas() != null) {
            resultadoDTO.setEtiquetas(
                    r.getEtiquetas()
                            .stream()
                            .map(Etiqueta::getNombreEtiqueta)
                            .toList()
            );
        }

        // DTO favorito
        FavoritoDTO dto = new FavoritoDTO();
        dto.setIdFavorito(favorito.getIdFavorito());
        dto.setResultado(resultadoDTO);

        return dto;
    }

    public List<FavoritoDTO> toDTOList(List<Favorito> favoritos, Usuario usuario) {
        return favoritos.stream()
                .map(f -> toDTO(f, usuario))
                .toList();
    }
}
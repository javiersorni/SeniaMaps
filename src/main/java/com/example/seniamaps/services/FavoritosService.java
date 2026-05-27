package com.example.seniamaps.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.seniamaps.dto.FavoritoDTO;
import com.example.seniamaps.dto.places.GeoapifyResponseDTO;
import com.example.seniamaps.entity.Usuario;
import com.example.seniamaps.mapper.FavoritoMapper;
import com.example.seniamaps.repository.FavoritoRepository;

@Service
public class FavoritosService {

    private final FavoritoRepository favoritoRepository;
    private final FavoritoMapper favoritoMapper;

    public FavoritosService(
            FavoritoRepository favoritoRepository,
            FavoritoMapper favoritoMapper) {

        this.favoritoRepository = favoritoRepository;
        this.favoritoMapper = favoritoMapper;
    }

    public List<FavoritoDTO> getFavoritos(Usuario usuario) {

        return favoritoRepository.findByUsuario(usuario)
                .stream()
                .map(f -> favoritoMapper.toDTO(f, usuario))
                .toList();
    }

    public void enrichFavoritos(GeoapifyResponseDTO response, Usuario usuario) {

        for (var feature : response.getFeatures()) {

            var props = feature.getProperties();

            boolean isFav = favoritoRepository
                    .existsByUsuarioAndResultadoIdLugar(usuario, props.getPlace_id());

            props.setEnFavorito(isFav);
        }
    }
}
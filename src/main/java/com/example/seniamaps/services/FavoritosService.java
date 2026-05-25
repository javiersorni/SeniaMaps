package com.example.seniamaps.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.seniamaps.dto.FavoritoDTO;
import com.example.seniamaps.dto.ResultadoDTO;
import com.example.seniamaps.entity.Favorito;
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
}
package com.example.seniamaps.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.seniamaps.dto.BusquedaDTO;
import com.example.seniamaps.entity.Busqueda;
import com.example.seniamaps.entity.Usuario;
import com.example.seniamaps.mapper.HistorialMapper;
import com.example.seniamaps.repository.BusquedaRepository;

@Service
public class HistorialService {

    private final BusquedaRepository busquedaRepository;
    private final HistorialMapper historialMapper;

    public HistorialService(
            BusquedaRepository busquedaRepository,
            HistorialMapper historialMapper) {

        this.busquedaRepository = busquedaRepository;
        this.historialMapper = historialMapper;
    }

    public List<BusquedaDTO> getHistorialUsuario(Usuario usuario) {

        List<Busqueda> busquedas = busquedaRepository.findByUsuario(usuario);

        return busquedas.stream()
                .map(b -> {
                    BusquedaDTO dto = new BusquedaDTO();

                    dto.setIdBusqueda(b.getIdBusqueda());
                    dto.setQuery(b.getQuery());
                    dto.setLatitud(b.getLatitud());
                    dto.setLongitud(b.getLongitud());
                    dto.setFechaBusqueda(b.getFechaBusqueda());

                    dto.setResultados(
                            historialMapper.toResultadosDTO(b, usuario)
                    );

                    return dto;
                })
                .toList();
    }
}
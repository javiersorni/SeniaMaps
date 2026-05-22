package com.example.seniamaps.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.seniamaps.dto.places.PlaceFeatureDTO;
import com.example.seniamaps.dto.places.PlacePropertiesDTO;
import com.example.seniamaps.entity.*;
import com.example.seniamaps.mapper.CategoriaMapper;
import com.example.seniamaps.repository.*;

@Service
public class ResultadoService {

    private final ResultadoRepository resultadoRepository;
    private final ResultadoBusquedaRepository resultadoBusquedaRepository;
    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    public ResultadoService(
            ResultadoRepository resultadoRepository,
            ResultadoBusquedaRepository resultadoBusquedaRepository,
            CategoriaRepository categoriaRepository,
            CategoriaMapper categoryMapper) {

        this.resultadoRepository = resultadoRepository;
        this.resultadoBusquedaRepository = resultadoBusquedaRepository;
        this.categoriaRepository = categoriaRepository;
        this.categoriaMapper = categoryMapper;
    }

    public void saveResults(List<PlaceFeatureDTO> features, Busqueda busqueda) {

        if (features == null) return;

        for (PlaceFeatureDTO feature : features) {

            PlacePropertiesDTO p = feature.getProperties();

            Resultado resultado = resultadoRepository
                    .findByIdLugar(p.getPlace_id())
                    .orElseGet(() -> {
                        Resultado r = new Resultado();
                        r.setIdLugar(p.getPlace_id());
                        return r;
                    });

            resultado.setNombre(p.getName());
            resultado.setDireccion(p.getFormatted());
            resultado.setRating(p.getRating());
            resultado.setLatitud(p.getLat());
            resultado.setLongitud(p.getLon());

            // categorías
            if (p.getCategories() != null) {

                List<Categoria> categorias = new ArrayList<>();

                for (String raw : p.getCategories()) {

                    if (!categoriaMapper.isValid(raw)) continue;

                    String clean = categoriaMapper.clean(raw);
                    if (clean.equals("Otros")) continue;

                    Categoria categoria = categoriaRepository
                            .findByNombreCategoria(clean)
                            .orElseGet(() -> {
                                Categoria c = new Categoria();
                                c.setNombreCategoria(clean);
                                return categoriaRepository.save(c);
                            });

                    categorias.add(categoria);
                }

                resultado.setCategorias(categorias);
            }

            resultado = resultadoRepository.save(resultado);

            boolean exists = resultadoBusquedaRepository
                    .existsByBusquedaAndResultado(busqueda, resultado);

            if (!exists) {
                ResultadoBusqueda rb = new ResultadoBusqueda();
                rb.setBusqueda(busqueda);
                rb.setResultado(resultado);
                resultadoBusquedaRepository.save(rb);
            }
        }
    }
}
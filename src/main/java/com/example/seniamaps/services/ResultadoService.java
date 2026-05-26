package com.example.seniamaps.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.seniamaps.dto.places.GeoapifyResponseDTO;
import com.example.seniamaps.dto.places.PlaceFeatureDTO;
import com.example.seniamaps.dto.places.PlacePropertiesDTO;
import com.example.seniamaps.entity.Busqueda;
import com.example.seniamaps.entity.Categoria;
import com.example.seniamaps.entity.Resultado;
import com.example.seniamaps.entity.ResultadoBusqueda;
import com.example.seniamaps.entity.Usuario;
import com.example.seniamaps.mapper.CategoriaMapper;
import com.example.seniamaps.repository.CategoriaRepository;
import com.example.seniamaps.repository.ResultadoBusquedaRepository;
import com.example.seniamaps.repository.ResultadoRepository;

@Service
public class ResultadoService {

    private final ResultadoRepository resultadoRepository;
    private final ResultadoBusquedaRepository resultadoBusquedaRepository;
    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;
    private final RatingService ratingService;

    public ResultadoService(
            ResultadoRepository resultadoRepository,
            ResultadoBusquedaRepository resultadoBusquedaRepository,
            CategoriaRepository categoriaRepository,
            CategoriaMapper categoryMapper,
            RatingService ratingService) {

        this.resultadoRepository = resultadoRepository;
        this.resultadoBusquedaRepository = resultadoBusquedaRepository;
        this.categoriaRepository = categoriaRepository;
        this.categoriaMapper = categoryMapper;
        this.ratingService = ratingService;
    }

    // ✅ ESTE TE FALTABA
    public void saveResults(List<PlaceFeatureDTO> features, Busqueda busqueda) {

        if (features == null)
            return;

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
            resultado.setLatitud(p.getLat());
            resultado.setLongitud(p.getLon());
            List<Categoria> categorias = new ArrayList<>();

            for (String cat : p.getCategories()) {

                String cleanCat = categoriaMapper.clean(cat);

                if (cleanCat == null || cleanCat.isBlank() || cleanCat.equals("Otros")) {
                    continue;
                }

                Categoria categoria = categoriaRepository.findByNombreCategoria(cleanCat)
                        .orElseGet(() -> {
                            Categoria c = new Categoria();
                            c.setNombreCategoria(cleanCat);
                            return categoriaRepository.save(c);
                        });

                categorias.add(categoria);
            }

            resultado.setCategorias(categorias);
            resultadoRepository.save(resultado);

            // 🔥 ESTO ES LO QUE TE FALTA
            if (!resultadoBusquedaRepository.existsByBusquedaAndResultado(busqueda, resultado)) {

                ResultadoBusqueda rb = new ResultadoBusqueda();
                rb.setBusqueda(busqueda);
                rb.setResultado(resultado);
                rb.setFechaConsulta(java.time.LocalDateTime.now());

                resultadoBusquedaRepository.save(rb);
            }
        }
    }

    // ✔️ TU ENRICH CORRECTO
    public void enrichRatings(GeoapifyResponseDTO response, Usuario usuario) {

        if (response == null || response.getFeatures() == null)
            return;

        for (PlaceFeatureDTO feature : response.getFeatures()) {

            PlacePropertiesDTO p = feature.getProperties();

            resultadoRepository.findByIdLugar(p.getPlace_id())
                    .ifPresent(resultado -> {

                        Double userRating = ratingService.getUserRating(usuario, resultado);

                        p.setUserRating(userRating);
                    });
        }
    }
}
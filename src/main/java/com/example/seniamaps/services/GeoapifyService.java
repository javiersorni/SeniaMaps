package com.example.seniamaps.services;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriUtils;

import com.example.seniamaps.dto.places.GeoapifyResponseDTO;
import com.example.seniamaps.dto.places.PlaceFeatureDTO;
import com.example.seniamaps.dto.places.PlacePropertiesDTO;
import com.example.seniamaps.entity.Resultado;
import com.example.seniamaps.entity.ResultadoBusqueda;
import com.example.seniamaps.repository.ResultadoRepository;



@Service
public class GeoapifyService {

    @Value("${geoapify.api.key}")
    private String apiKey;

    private final RestClient restClient = RestClient.create();

    private final ResultadoRepository resultadoRepository;

    public GeoapifyService(ResultadoRepository resultadoRepository) {
        this.resultadoRepository = resultadoRepository;
    }

    /*
     * =====================================================
     * PLACES SEARCH (API CALL)
     * =====================================================
     */

    public GeoapifyResponseDTO searchPlaces(
            double lat,
            double lon,
            String keyword
    ) {

        String encodedKeyword =
                UriUtils.encode(keyword, StandardCharsets.UTF_8);

        String url =
                "https://api.geoapify.com/v2/places"
                        + "?categories=" + mapKeywordToCategory(keyword)
                        + "&name=" + encodedKeyword
                        + "&filter=circle:" + lon + "," + lat + ",3000"
                        + "&limit=20"
                        + "&apiKey=" + apiKey;

        System.out.println("🌍 GEOAPIFY URL: " + url);

        return restClient.get()
                .uri(url)
                .retrieve()
                .body(GeoapifyResponseDTO.class);
    }

    /*
     * =====================================================
     * CACHE → BUILD RESPONSE FROM DATABASE
     * =====================================================
     */

    public GeoapifyResponseDTO buildResponseFromDatabase(
            List<ResultadoBusqueda> relaciones
    ) {

        GeoapifyResponseDTO response = new GeoapifyResponseDTO();

        List<PlaceFeatureDTO> features = new ArrayList<>();

        for (ResultadoBusqueda rb : relaciones) {

            Resultado r = rb.getResultado();

            PlacePropertiesDTO props = new PlacePropertiesDTO();

            props.setPlace_id(r.getIdLugar());
            props.setName(r.getNombre());
            props.setFormatted(r.getDireccion());
            props.setLat(r.getLatitud());
            props.setLon(r.getLongitud());
            props.setRating(r.getRating());

            PlaceFeatureDTO feature = new PlaceFeatureDTO();
            feature.setProperties(props);

            features.add(feature);
        }

        response.setFeatures(features);

        return response;
    }

    /*
     * =====================================================
     * CATEGORY MAPPING
     * =====================================================
     */

    private String mapKeywordToCategory(String keyword) {

        keyword = keyword.toLowerCase();

        return switch (keyword) {

            case "pizza" -> "catering.restaurant";
            case "cafe", "cafeteria" -> "catering.cafe";
            case "hotel" -> "accommodation.hotel";
            case "hospital" -> "healthcare.hospital";
            case "farmacia" -> "healthcare.pharmacy";

            case "mercadona",
                 "consum",
                 "aldi",
                 "lidl",
                 "carrefour" ->
                    "commercial.supermarket";

            default -> "commercial";
        };
    }

    /*
     * =====================================================
     * CACHE TIME (SI LO USAS EN CONTROLLER)
     * =====================================================
     */

    public LocalDateTime getCacheTime(String keyword) {

        keyword = keyword.toLowerCase();

        return switch (keyword) {

            case "pizza",
                 "bar",
                 "cafeteria",
                 "cafe" ->
                    LocalDateTime.now().minusHours(1);

            case "mercadona",
                 "consum",
                 "aldi",
                 "lidl",
                 "carrefour" ->
                    LocalDateTime.now().minusHours(24);

            case "farmacia" ->
                    LocalDateTime.now().minusHours(12);

            default ->
                    LocalDateTime.now().minusHours(2);
        };
    }
}
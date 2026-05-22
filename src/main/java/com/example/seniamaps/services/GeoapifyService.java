package com.example.seniamaps.services;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriUtils;

import com.example.seniamaps.dto.places.GeoapifyResponseDTO;
import com.example.seniamaps.mapper.CategoriaMapper;

@Service
public class GeoapifyService {

    @Value("${geoapify.api.key}")
    private String apiKey;

    private final RestClient restClient = RestClient.create();
    private final CategoriaMapper categoryMapper;

    public GeoapifyService(CategoriaMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    public GeoapifyResponseDTO searchPlaces(
            double lat,
            double lon,
            String keyword,
            int radius,
            int limit) {

        String encodedKeyword =
                UriUtils.encode(keyword, StandardCharsets.UTF_8);

        String url = "https://api.geoapify.com/v2/places"
                + "?categories=" + categoryMapper.mapToGeoapify(keyword)
                + "&name=" + encodedKeyword
                + "&filter=circle:" + lon + "," + lat + "," + radius
                + "&limit=" + limit
                + "&apiKey=" + apiKey;

        return restClient.get()
                .uri(url)
                .retrieve()
                .body(GeoapifyResponseDTO.class);
    }
}
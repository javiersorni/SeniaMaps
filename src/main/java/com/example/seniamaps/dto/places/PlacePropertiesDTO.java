package com.example.seniamaps.dto.places;

import java.util.List;

import lombok.Data;

@Data
public class PlacePropertiesDTO {

    private String place_id;

    private String name;

    private String formatted;

    private Double lat;

    private Double lon;

    private Double userRating;

    private List<String> categories;
}
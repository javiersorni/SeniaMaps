package com.example.seniamaps.dto.places;

import lombok.Data;

@Data
public class PlacePropertiesDTO {

    private String place_id;

    private String name;

    private String formatted;

    private Double lat;

    private Double lon;

    private Double rating;

    private String tipo;
}
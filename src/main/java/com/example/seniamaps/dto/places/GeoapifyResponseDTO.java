package com.example.seniamaps.dto.places;

import java.util.List;

import lombok.Data;

@Data
public class GeoapifyResponseDTO {

    private List<PlaceFeatureDTO> features;
}
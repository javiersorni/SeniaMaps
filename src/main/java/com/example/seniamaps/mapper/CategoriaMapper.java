package com.example.seniamaps.mapper;

import org.springframework.stereotype.Component;

@Component
public class CategoriaMapper {

    public String mapToGeoapify(String keyword) {

        keyword = keyword.toLowerCase();

        return switch (keyword) {
            case "pizza", "hamburguesa" -> "catering.restaurant";
            case "cafe", "cafeteria" -> "catering.cafe";
            case "hotel", "hostal" -> "accommodation.hotel";
            case "hospital" -> "healthcare.hospital";
            case "farmacia" -> "healthcare.pharmacy";
            case "cine" -> "entertainment.cinema";
            case "mercadona",
                    "consum",
                    "aldi",
                    "lidl",
                    "dia",
                    "dialprix",
                    "carrefour" ->
                "commercial.supermarket";

            default -> "commercial";
        };
    }

    public String clean(String raw) {

        if (raw == null || raw.isBlank())
            return "Otros";

        raw = raw.toLowerCase();

        if (raw.contains("pharmacy"))
            return "Farmacia";
        if (raw.contains("hospital"))
            return "Hospital";
        if (raw.contains("restaurant"))
            return "Restaurante";
        if (raw.contains("cafe"))
            return "Café";
        if (raw.contains("fast_food"))
            return "Comida rápida";
        if (raw.contains("supermarket"))
            return "Supermercado";
        if (raw.contains("shopping_mall"))
            return "Centro comercial";
        if (raw.contains("cinema"))
            return "Cine";
        if (raw.contains("hotel"))
            return "Hotel";

        return "Otros";
    }

    public boolean isValid(String raw) {

        return raw != null &&
                (raw.startsWith("catering.") ||
                        raw.startsWith("healthcare.") ||
                        raw.startsWith("commercial.") ||
                        raw.startsWith("accommodation.") ||
                        raw.startsWith("entertainment."));
    }
}
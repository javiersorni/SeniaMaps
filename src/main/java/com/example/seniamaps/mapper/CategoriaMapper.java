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

            case "mercadona",
                 "consum",
                 "aldi",
                 "lidl",
                 "dia",
                 "dialprix",
                 "carrefour"
                    -> "commercial.supermarket";

            default -> "commercial";
        };
    }

    public String clean(String raw) {

        if (raw == null) return "Otros";

        raw = raw.toLowerCase();

        return switch (raw) {

            case "catering.restaurant", "restaurant" -> "Restaurante";
            case "catering.cafe", "catering.coffee_shop" -> "Café";
            case "catering.fast_food", "fast_food" -> "Comida rápida";

            case "accommodation.hotel" -> "Hotel";
            case "healthcare.pharmacy" -> "Farmacia";
            case "healthcare.hospital" -> "Hospital";

            case "commercial.supermarket" -> "Supermercado";
            case "commercial.shopping_mall" -> "Centro comercial";

            case "entertainment.cinema" -> "Cine";

            default -> "Otros";
        };
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
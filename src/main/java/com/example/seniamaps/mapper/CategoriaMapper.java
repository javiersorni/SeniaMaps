package com.example.seniamaps.mapper;

import org.springframework.stereotype.Component;

@Component
public class CategoriaMapper {

    /**
     * Convierte keywords del usuario a categoría Geoapify
     */
    public String mapToGeoapify(String keyword) {

        if (keyword == null || keyword.isBlank()) {
            return "commercial";
        }

        keyword = keyword.toLowerCase();

        return switch (keyword) {

            // RESTAURACIÓN
            case "pizza", "hamburguesa", "burger", "kebab", "tacos" -> "catering.restaurant";
            case "cafe", "cafeteria", "coffee" -> "catering.cafe";
            case "bar", "pub" -> "catering.bar";

            // ALOJAMIENTO
            case "hotel", "hostal", "hostel", "motel" -> "accommodation.hotel";
            case "apartamento", "apartment" -> "accommodation.apartment";

            // SALUD
            case "hospital", "clinica", "clinic" -> "healthcare.hospital";
            case "farmacia", "pharmacy" -> "healthcare.pharmacy";
            case "dentista" -> "healthcare.dentist";

            // OCIO
            case "cine", "cinema" -> "entertainment.cinema";
            case "museo", "museum" -> "entertainment.museum";
            case "parque", "park" -> "leisure.park";

            // TRANSPORTE / SERVICIOS
            case "parking" -> "parking.cars";
            case "gasolinera", "fuel" -> "service.vehicle.fuel";

            // SUPERMERCADOS / TIENDAS
            case "mercadona", "lidl", "aldi", "carrefour", "consum", "dia" ->
                "commercial.supermarket";

            // FITNESS / DEPORTE
            case "gimnasio", "gym", "fitness" -> "sport.fitness.gym";

            default -> "commercial";
        };
    }

    /**
     * Normaliza categorías Geoapify a etiquetas legibles
     */
    public String clean(String raw) {

        if (raw == null || raw.isBlank()) {
            return "Otros";
        }

        raw = raw.toLowerCase();

        if (raw.contains("pharmacy"))
            return "Farmacia";
        if (raw.contains("hospital"))
            return "Hospital";
        if (raw.contains("dentist"))
            return "Dentista";

        if (raw.contains("restaurant"))
            return "Restaurante";
        if (raw.contains("cafe"))
            return "Café";
        if (raw.contains("bar"))
            return "Bar";

        if (raw.contains("fast_food"))
            return "Comida rápida";

        if (raw.contains("supermarket") || raw.contains("commercial")) {
            return "Supermercado / Tienda";
        }

        if (raw.contains("cinema"))
            return "Cine";
        if (raw.contains("museum"))
            return "Museo";
        if (raw.contains("park"))
            return "Parque";

        if (raw.contains("hotel") || raw.contains("accommodation")) {
            return "Alojamiento";
        }

        if (raw.contains("gym") || raw.contains("fitness")) return "Gimnasio";


        return "Otros";
    }

    /**
     * Valida si la categoría pertenece a dominios soportados
     */
    public boolean isValid(String raw) {

        return raw != null && (raw.startsWith("catering.") ||
                raw.startsWith("healthcare.") ||
                raw.startsWith("commercial.") ||
                raw.startsWith("accommodation.") ||
                raw.startsWith("entertainment.") ||
                raw.startsWith("leisure.") ||
                raw.startsWith("parking.") ||
                raw.startsWith("service."));
    }
}
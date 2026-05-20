package com.example.seniamaps.services;

import java.util.List;

public class GeoapifyResponse {

    private List<Feature> features;

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    public static class Feature {

        private Properties properties;

        public Properties getProperties() {
            return properties;
        }

        public void setProperties(Properties properties) {
            this.properties = properties;
        }
    }

    public static class Properties {

        private String name;
        private String formatted;
        private Double lat;
        private Double lon;

        public String getName() {
            return name;
        }

        public String getFormatted() {
            return formatted;
        }

        public Double getLat() {
            return lat;
        }

        public Double getLon() {
            return lon;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setFormatted(String formatted) {
            this.formatted = formatted;
        }

        public void setLat(Double lat) {
            this.lat = lat;
        }

        public void setLon(Double lon) {
            this.lon = lon;
        }
    }
}
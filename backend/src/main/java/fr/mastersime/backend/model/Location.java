package fr.mastersime.backend.model;


import jakarta.persistence.*;

@Embeddable
public class Location {

    private Double longitude;
    private Double latitude;
    // Getters and setters

    // No-argument constructor
    public Location() {
    }

    public Location(Double longitude, Double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    // Getters

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    // Setters

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

}

package fr.mastersime.backend.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class PhotoData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String image;
    @Embedded
    private Location location;
    private String type;
    private LocalDateTime dateTime;

    public PhotoData(String image, Location location, String type) {
        this.image = image;
        this.location = location;
        this.type = type;
        this.dateTime = LocalDateTime.now();
    }


    // No-argument constructor
    public PhotoData() {
        this.dateTime = LocalDateTime.now();
    }
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public Location getLocation() {
        return location;
    }

    public String getType() {
        return type;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setType(String type) {
        this.type = type;
    }
}

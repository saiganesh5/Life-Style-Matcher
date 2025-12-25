package com.ganesh.LifeStyleMatcherProject;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
public class Neighborhood {
    // Getters and Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String city;
    private int safetyRating;
    private boolean gymAccess;
    private int foodOutlets;
    private int avgRent;
    private int walkabilityScore;
    private int greenSpaces;

    private int internetConnectivity;
    private int transportScore;
    private int noiseLevel;
    private boolean hospitalAccess;
    private int marketScore;
    private boolean petFriendly;

    private boolean schoolAccess;
    private int collegeProximity;
    private int trafficCongestionLevel;
    private int airQualityIndex;
    private int publicRating;

    @OneToMany(mappedBy = "neighborhood")
    private List<Listing> listing;
}

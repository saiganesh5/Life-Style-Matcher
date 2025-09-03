package com.ganesh.LifeStyleMatcherProject;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "listing")
@Getter
@Setter
@NoArgsConstructor
public class Listing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;
    private String location;

    @Enumerated(EnumType.STRING)
    private ListingType type;

    @Column(name = "bhk_or_room_type")
    private String bhkOrRoomType;

    private String rent;
    private String area;

    private String furnishing;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String source;

    @Column(name = "rent_min")
    private Integer rentMin;

    @Column(name = "rent_max")
    private Integer rentMax;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "neighborhood_id")
    private Neighborhood neighborhood;

    public enum ListingType {
        Flat,
        PG
    }

    public enum Gender {
        Male,
        Female,
        Both
    }
}
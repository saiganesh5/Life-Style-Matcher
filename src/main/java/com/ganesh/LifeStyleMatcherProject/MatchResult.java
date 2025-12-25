package com.ganesh.LifeStyleMatcherProject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchResult {
    private String name;
    private String city;
    private int score;
    private int rent;
    private String type;
    private String bhkOrRoomType;
    private String location;
}


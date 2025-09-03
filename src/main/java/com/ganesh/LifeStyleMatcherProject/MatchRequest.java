package com.ganesh.LifeStyleMatcherProject;

import lombok.Data;

@Data
public class MatchRequest {
    private int budget;
    private boolean gym;
    private boolean hospital;
    private boolean pet;

    private Priorities priorities;

    @Data
    public static class Priorities {
        private int safety;
        private int walkability;
        private int food;
        private int internet;
        private int transport;
        private int market;
        private int noise;
    }
}


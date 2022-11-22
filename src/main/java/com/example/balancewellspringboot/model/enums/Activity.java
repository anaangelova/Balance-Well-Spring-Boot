package com.example.balancewellspringboot.model.enums;

public enum Activity {
    SEDENTARY("Sedentary", 1.2),
    LIGHT("Light", 1.375),
    MODERATE("Moderate", 1.55),
    ACTIVE("Active", 1.725);

    private final String displayName;
    private final Double factor;

    Activity(String displayName, Double factor) {
        this.displayName = displayName;
        this.factor = factor;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public Double getFactor() {
        return factor;
    }
}

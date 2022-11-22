package com.example.balancewellspringboot.model.enums;

public enum Goal {
    WEIGHT_LOSS("Weight loss"),
    TONE_AND_MAINTAIN("Tone and maintain"),
    MUSCLE_GAIN("Muscle gain");

    private final String displayName;

    Goal(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}

package com.example.balancewellspringboot.model.dto;

import com.example.balancewellspringboot.model.enums.MealEnum;
import lombok.Data;

import java.util.List;

@Data
public class ContentDTO {
    private List<MealEnum> allMeals;
    private List<String> allMeasurements;

    public ContentDTO(List<MealEnum> allMeals, List<String> allMeasurements) {
        this.allMeals = allMeals;
        this.allMeasurements = allMeasurements;
    }
}

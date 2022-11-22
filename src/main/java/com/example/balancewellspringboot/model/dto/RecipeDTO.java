package com.example.balancewellspringboot.model.dto;

import com.example.balancewellspringboot.model.enums.RecipeIngredientMeasurement;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class RecipeDTO {
    private String title;
    private String description;
    private String author;
    private List<String> instructions;
    private String meal;
    private List<String> ingredientNames;
    private List<Double> ingredientQuantities;
    private List<RecipeIngredientMeasurement> ingredientMeasurements;
    private Double prepH;
    private Double prepM;
    private Double cookH;
    private Double cookM;
    private Double caloriesRecipe;
}

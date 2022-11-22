package com.example.balancewellspringboot.model.dto;

import lombok.Data;

@Data
public class RecipeAddToMealRequestDTO {
    private String selectedDate;
    private String meal;
    private int recipeId;
    private double caloriesRecipe;
}

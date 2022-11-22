package com.example.balancewellspringboot.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class RecipeAddToMealDTO {
    private Long id;
    private String title;
    private String image;
    private Double caloriesInRecipe;
    List<String> ingredientList;
}

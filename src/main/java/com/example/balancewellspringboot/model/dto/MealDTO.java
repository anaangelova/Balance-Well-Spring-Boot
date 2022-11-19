package com.example.balancewellspringboot.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class MealDTO {
    private String name;
    private int caloriesInMeal;
    private List<IngredientDTO> ingredientList;
}

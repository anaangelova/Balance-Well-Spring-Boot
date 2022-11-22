package com.example.balancewellspringboot.model.dto.edamamApi.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EdamamFoodDetailResponseDTO {
    private int calories;
    private EdamamTotalNutrientsDTO totalNutrients;
    private List<EdamamIngredientInfoDTO> ingredients;
}

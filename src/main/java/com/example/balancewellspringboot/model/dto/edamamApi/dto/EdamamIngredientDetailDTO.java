package com.example.balancewellspringboot.model.dto.edamamApi.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EdamamIngredientDetailDTO {
    private EdamamFoodDetailDTO food;
}

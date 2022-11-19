package com.example.balancewellspringboot.model.dto.edamamApi.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class EdamamFoodQuantityMeasureDTO {
    List<EdamamIngredientDataDTO> ingredients;
}

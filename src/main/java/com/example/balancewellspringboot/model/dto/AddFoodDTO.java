package com.example.balancewellspringboot.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddFoodDTO {
    private int ingredientQuantities;
    private String ingredientMeasurements;
}

package com.example.balancewellspringboot.model.dto.edamamApi.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EdamamMacronutrientDTO {
    private String label;
    private double quantity;
    private String unit;
}

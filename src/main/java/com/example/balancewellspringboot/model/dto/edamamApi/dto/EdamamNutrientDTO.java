package com.example.balancewellspringboot.model.dto.edamamApi.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EdamamNutrientDTO {
    private String ENERC_KCAL;
    private String PROCNT;
    private String FAT;
    private String CHOCDF;
    private String FIBTG;
}

package com.example.balancewellspringboot.model.dto.edamamApi.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EdamamTotalNutrientsDTO {
    private EdamamMacronutrientDTO FAT;
    private EdamamMacronutrientDTO PROCNT;
    private EdamamMacronutrientDTO CHOCDF;
    private EdamamMacronutrientDTO FIBTG;
    private EdamamMacronutrientDTO SUGAR;

}

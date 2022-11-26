package com.example.balancewellspringboot.model.dto.edamamApi.dto.response;

import lombok.Builder;
import lombok.Data;



@Builder
@Data
public class EdamamFoodDetailDTO {
    private String foodId;
    private EdamamNutrientDTO nutrients;
    String image;

}

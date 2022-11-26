package com.example.balancewellspringboot.model.dto.edamamApi.dto.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EdamamIngredientDataDTO {
    private int quantity;
    private String measureURI;
    private String foodId;
}

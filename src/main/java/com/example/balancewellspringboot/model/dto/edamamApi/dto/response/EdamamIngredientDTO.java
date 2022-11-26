package com.example.balancewellspringboot.model.dto.edamamApi.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class EdamamIngredientDTO {
    private String text;
    private List<EdamamIngredientDetailDTO> parsed;
    private List<EdamamHintsDTO> hints;
}

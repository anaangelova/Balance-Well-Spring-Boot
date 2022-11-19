package com.example.balancewellspringboot.model.dto.edamamApi.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EdamamIngredientInfoDTO {
    private List<EdamamParsedDTO> parsed;
}

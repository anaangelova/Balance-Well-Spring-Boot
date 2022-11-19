package com.example.balancewellspringboot.model.dto.edamamApi.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class EdamamHintsDTO {
    private EdamamFoodDetailDTO food;
    private List<EdamamMeasuresDTO> measures;
}

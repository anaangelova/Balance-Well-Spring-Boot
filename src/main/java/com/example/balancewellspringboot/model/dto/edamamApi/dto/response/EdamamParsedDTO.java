package com.example.balancewellspringboot.model.dto.edamamApi.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EdamamParsedDTO {
    private double quantity;
    private String measure;
    private String food;
    private String foodId;

}

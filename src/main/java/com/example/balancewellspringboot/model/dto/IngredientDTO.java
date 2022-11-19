package com.example.balancewellspringboot.model.dto;

import com.example.balancewellspringboot.model.Measurement;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class IngredientDTO {
    private String name;
    private Double quantity;
    private String measurement;
    private Double caloriesInIngredient;
    public String getFormatted(){
        return String.format("%.0f %s %s",this.quantity,this.measurement,this.name);
    }
}

package com.example.balancewellspringboot.model.dto;

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
    public int getCalories() {
        return (int) Math.round(caloriesInIngredient);
    }
}

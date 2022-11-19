package com.example.balancewellspringboot.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class FoodDetailDTO {
    private String foodId;
    private String foodName;
    private String foodImage;
    private double calories;
    private double proteinGrams;
    private double fatGrams;
    private double carbGrams;
    private double fiberGrams;
    private List<String> measures;
}

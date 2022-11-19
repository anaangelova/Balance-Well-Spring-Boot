package com.example.balancewellspringboot.service.interfaces;

import com.example.balancewellspringboot.model.dto.FoodDetailDTO;
import com.example.balancewellspringboot.model.dto.edamamApi.dto.EdamamIngredientDTO;

public interface FoodService {
    FoodDetailDTO getFoodDetailDTO(EdamamIngredientDTO edamamIngredientDTO);
}

package com.example.balancewellspringboot.service.interfaces;

import com.example.balancewellspringboot.model.Ingredient;
import com.example.balancewellspringboot.model.dto.FoodDetailDTO;
import com.example.balancewellspringboot.model.dto.edamamApi.dto.EdamamFoodDetailResponseDTO;
import com.example.balancewellspringboot.model.dto.edamamApi.dto.EdamamIngredientDTO;

public interface FoodService {
    FoodDetailDTO getFoodDetailDTO(EdamamIngredientDTO edamamIngredientDTO);
    Ingredient createIngredient(EdamamFoodDetailResponseDTO foodDTO, String date, String currentUser, String meal);
}

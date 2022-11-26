package com.example.balancewellspringboot.service.interfaces;

import com.example.balancewellspringboot.model.Ingredient;
import com.example.balancewellspringboot.model.dto.AddFoodDTO;
import com.example.balancewellspringboot.model.dto.FoodDetailDTO;
import com.example.balancewellspringboot.model.dto.IngredientDTO;
import com.example.balancewellspringboot.model.dto.RecipeAddToMealRequestDTO;
import com.example.balancewellspringboot.model.dto.edamamApi.dto.response.EdamamFoodDetailResponseDTO;
import com.example.balancewellspringboot.model.dto.edamamApi.dto.response.EdamamIngredientDTO;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface FoodService {

    FoodDetailDTO getFoodDetailDTO(EdamamIngredientDTO edamamIngredientDTO);
    Ingredient createIngredient(EdamamFoodDetailResponseDTO foodDTO, String foodName, String date, String currentUser, String meal);
    Ingredient editIngredient(EdamamFoodDetailResponseDTO foodDTO, String date, String currentUser, String meal, String ingrId);
    List<String> searchForIngredientsFromSearchField(String searchInput, String date, String meal) throws IOException, InterruptedException;
    EdamamIngredientDTO getDetailsDTOForSelectedIngredient(String date, String meal, String foodName) throws IOException, InterruptedException;
    EdamamFoodDetailResponseDTO saveSelectedIngredient(String date, String meal, String foodId, AddFoodDTO addFoodDTO) throws IOException, InterruptedException;
    IngredientDTO getIngredientDTO(String currentUser, LocalDate date, String meal, String ingrId);
    void deleteIngredientById(String ingrId, String currentUser, LocalDate date, String meal);

    Ingredient addRecipeToMeal(RecipeAddToMealRequestDTO recipeDTO, Long recipeId, String currentUser);

}

package com.example.balancewellspringboot.service.interfaces;

import com.example.balancewellspringboot.model.Recipe;
import com.example.balancewellspringboot.model.dto.RecipeAddToMealDTO;
import com.example.balancewellspringboot.model.dto.RecipeDTO;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface RecipeService {
    Recipe findById(Long id);

    List<Recipe> findAllRecipesMostRecent();

    List<Recipe> findAllRecipesTrending();

    List<Recipe> findAllRecipesByUser(String username);

    Optional<Recipe> save(RecipeDTO recipeDTO, List<String> imagesNames) throws IOException;

    void deleteById(Long id);

    Optional<Recipe> editRecipe(RecipeDTO recipeDTO, Long recipeId);

    List<Recipe> findBySearch(String search);

    RecipeAddToMealDTO getRecipeAddToMealDTO(Long id);

}

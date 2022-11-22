package com.example.balancewellspringboot.repository;

import com.example.balancewellspringboot.model.Recipe;
import com.example.balancewellspringboot.model.enums.MealEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Optional<List<Recipe>> findByTitleContainingIgnoreCase(String title);

    @Query(value = "SELECT * FROM Recipe ORDER BY date_created desc LIMIT 4",nativeQuery = true)
    List<Recipe> findMostRecentRecipes();

    List<Recipe> findAllByAuthor_Username(String username);

    Optional<List<Recipe>> findAllByMeal(MealEnum mealEnum);


}

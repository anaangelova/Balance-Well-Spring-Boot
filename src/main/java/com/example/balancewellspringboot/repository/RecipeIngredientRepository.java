package com.example.balancewellspringboot.repository;

import com.example.balancewellspringboot.model.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {
}

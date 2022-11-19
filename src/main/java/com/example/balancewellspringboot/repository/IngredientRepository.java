package com.example.balancewellspringboot.repository;

import com.example.balancewellspringboot.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}

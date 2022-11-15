package com.example.balancewellspringboot.repository;

import com.example.balancewellspringboot.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealRepository extends JpaRepository<Meal, Long> {
}

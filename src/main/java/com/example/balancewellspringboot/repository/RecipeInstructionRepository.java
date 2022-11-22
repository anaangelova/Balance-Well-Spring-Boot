package com.example.balancewellspringboot.repository;

import com.example.balancewellspringboot.model.RecipeInstruction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeInstructionRepository extends JpaRepository<RecipeInstruction, Long> {
}

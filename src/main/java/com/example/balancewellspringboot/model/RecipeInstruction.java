package com.example.balancewellspringboot.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
public class RecipeInstruction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    @ManyToOne
    private Recipe recipe;

    public RecipeInstruction(){}

    public RecipeInstruction(Long id, String description, Recipe recipe) {
        this.id = id;
        this.description = description;
        this.recipe = recipe;
    }

    public RecipeInstruction(String description, Recipe recipe) {
        this.description = description;
        this.recipe = recipe;
    }
}

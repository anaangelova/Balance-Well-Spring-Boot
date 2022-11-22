package com.example.balancewellspringboot.model;

import com.example.balancewellspringboot.model.identity.EndUser;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private Double prepInMins;
    private Double cookInMins;
    private Double caloriesInRecipe;
    private LocalDateTime dateCreated;
    @OneToMany(mappedBy = "recipe")
    private List<RecipeIngredient> recipeIngredientList;
    @OneToMany(mappedBy = "recipe")
    private List<RecipeInstruction> instructions;
    @Enumerated(EnumType.STRING)
    private MealEnum meal;
    @ManyToOne
    private EndUser author;
    @OneToMany(mappedBy = "recipeOwner")
    private List<RecipeImage> images;

    public Recipe(Long id, String title, String description, Double prepInMins, Double cookInMins, Double caloriesInRecipe, LocalDateTime dateCreated, List<RecipeIngredient> recipeIngredientList, List<RecipeInstruction> instructions, MealEnum meal, EndUser author, List<RecipeImage> images) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.prepInMins = prepInMins;
        this.cookInMins = cookInMins;
        this.caloriesInRecipe = caloriesInRecipe;
        this.dateCreated = dateCreated;
        this.recipeIngredientList = recipeIngredientList;
        this.instructions = instructions;
        this.meal = meal;
        this.author = author;
        this.images = images;
    }

    public Recipe() {}
}

package com.example.balancewellspringboot.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class RecipeImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @ManyToOne
    private Recipe recipeOwner;

    public RecipeImage(String title, Recipe recipeOwner) {
        this.title = title;
        this.recipeOwner = recipeOwner;
    }

    public RecipeImage() {}
}

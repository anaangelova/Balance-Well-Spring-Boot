package com.example.balancewellspringboot.model;

import com.example.balancewellspringboot.model.enums.RecipeIngredientMeasurement;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
public class RecipeIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double quantity;
    @ManyToOne
    private Recipe recipe;
    @Enumerated(EnumType.STRING)
    private RecipeIngredientMeasurement measurement;

    public RecipeIngredient(Long id, String name, Double quantity, Recipe recipe, RecipeIngredientMeasurement measurement) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.recipe = recipe;
        this.measurement = measurement;
    }

    public  RecipeIngredient() {

    }

    public RecipeIngredient(String name, Double quantity, RecipeIngredientMeasurement measurement, Recipe recipe) {
        this.name = name;
        this.quantity = quantity;
        this.measurement = measurement;
        this.recipe = recipe;
    }

    public String getFormatted(){
        return String.format("%.0f %s %s",this.quantity,this.measurement.name(),this.name);
    }
}

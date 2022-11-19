package com.example.balancewellspringboot.model;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;


@Entity
@Builder
@Getter
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String foodIdApi;
    private Double quantity;
    private String measurement;
    private Double caloriesInIngredient;
    @ManyToOne
    private Meal meal;

    public Ingredient(){}

    public Ingredient(Long id, String name, String foodIdApi, Double quantity, String measurement, Double caloriesInIngredient, Meal meal) {
        this.id = id;
        this.name = name;
        this.foodIdApi = foodIdApi;
        this.quantity = quantity;
        this.measurement = measurement;
        this.caloriesInIngredient = caloriesInIngredient;
        this.meal = meal;
    }

    public String getFormatted(){
        return String.format("%.0f %s %s",this.quantity,this.measurement,this.name);
    }

}

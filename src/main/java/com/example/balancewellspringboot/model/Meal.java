package com.example.balancewellspringboot.model;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;


@Entity
@Builder
@Getter
public class Meal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double caloriesInMeal;
    @ManyToOne
    private LoggedDay loggedDay;
    @OneToMany(mappedBy = "meal")
    private List<Ingredient> ingredientList;

    public Meal(){}

    public Meal(Long id, String name, Double caloriesInMeal, LoggedDay loggedDay, List<Ingredient> ingredientList) {
        this.id = id;
        this.name = name;
        this.caloriesInMeal = caloriesInMeal;
        this.loggedDay = loggedDay;
        this.ingredientList = ingredientList;
    }

    public void updateCaloriesInMeal() {
        ingredientList.forEach(i -> caloriesInMeal = caloriesInMeal + i.getCaloriesInIngredient());
    }
}

package com.example.balancewellspringboot.model;

import com.example.balancewellspringboot.model.enums.MealEnum;
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
    @Enumerated(EnumType.STRING)
    private MealEnum name;
    private Double caloriesInMeal;
    @ManyToOne
    private LoggedDay loggedDay;
    @OneToMany(mappedBy = "meal")
    private List<Ingredient> ingredientList;

    public Meal(){}

    public Meal(Long id, MealEnum name, Double caloriesInMeal, LoggedDay loggedDay, List<Ingredient> ingredientList) {
        this.id = id;
        this.name = name;
        this.caloriesInMeal = caloriesInMeal;
        this.loggedDay = loggedDay;
        this.ingredientList = ingredientList;
    }

    public void updateCaloriesInMeal() {
        caloriesInMeal = ingredientList.stream().map(Ingredient::getCaloriesInIngredient).reduce(0.0, (a, b) -> a + b);
    }
}

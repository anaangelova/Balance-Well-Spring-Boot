package com.example.balancewellspringboot.model.dto;

import com.example.balancewellspringboot.model.Recipe;
import lombok.Data;

import java.util.List;

@Data
public class RecipeDetailsDTO {
    private List<String> allInstr;
    private List<String> allIngr;
    private String formatted;
    private Recipe recipe;

    public RecipeDetailsDTO(List<String> allInstr, List<String> allIngr, String formatted, Recipe recipe) {
        this.allInstr = allInstr;
        this.allIngr = allIngr;
        this.formatted = formatted;
        this.recipe = recipe;
    }
}

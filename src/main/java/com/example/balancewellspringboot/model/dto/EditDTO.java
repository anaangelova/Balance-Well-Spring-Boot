package com.example.balancewellspringboot.model.dto;

import com.example.balancewellspringboot.model.Recipe;
import lombok.Data;

@Data
public class EditDTO {
    private Recipe recipe;
    private int prepH;
    private int cookH;

    public EditDTO(Recipe recipe, int prepH, int cookH) {
        this.recipe = recipe;
        this.prepH = prepH;
        this.cookH = cookH;
    }
}

package com.example.balancewellspringboot.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class LoggedDayDTO {
    private String dateForDay;
    private Long targetCalories;
    private Long totalCalories;
    private List<MealDTO> mealList;
}

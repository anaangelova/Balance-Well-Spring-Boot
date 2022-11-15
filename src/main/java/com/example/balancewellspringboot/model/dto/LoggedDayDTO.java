package com.example.balancewellspringboot.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
public class LoggedDayDTO {
    private LocalDateTime dateForDay;
    private Long targetCalories;
    private Long totalCalories;
    private List<MealDTO> mealList;
}

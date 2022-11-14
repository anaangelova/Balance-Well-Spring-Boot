package com.example.balancewellspringboot.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class LoggedDayDTO {
    private LocalDateTime dateForDay;
    private Long targetCalories;
    private Long totalCalories;
    //TODO add meals
}

package com.example.balancewellspringboot.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CaloriesGoalDTO {
    private int status;
    private String message;
}

package com.example.balancewellspringboot.model.dto;

import lombok.Data;


@Data
public class LineChartDTO {
    int year;
    int month;
    int day;
    Double weight;
    String image;

    public LineChartDTO(int year, int month, int day, Double weight, String image) {
        this.weight = weight;
        this.year = year;
        this.month = month;
        this.day = day;
        this.image = image;
    }
}

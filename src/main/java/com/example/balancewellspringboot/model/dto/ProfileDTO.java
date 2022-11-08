package com.example.balancewellspringboot.model.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ProfileDTO {
    @NotEmpty
    private Double height;
    @NotEmpty
    private Double weight;
    @NotEmpty
    private Integer age;
    @NotEmpty
    private boolean sex;
    @NotEmpty
    private String goal;
    @NotEmpty
    private String activity;
    @NotEmpty
    private Double goalWeight;
    String endUser;
}

package com.example.balancewellspringboot.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class BMI {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    Double value;
    String range;
}

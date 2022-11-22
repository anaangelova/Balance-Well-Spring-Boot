package com.example.balancewellspringboot.model;

import com.example.balancewellspringboot.model.enums.Activity;
import com.example.balancewellspringboot.model.enums.Goal;
import com.example.balancewellspringboot.model.enums.Sex;
import com.example.balancewellspringboot.model.identity.EndUser;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private EndUser endUser;
    private Integer age;
    @Enumerated(EnumType.STRING)
    private Sex sex;
    private Double height;
    private Double weight;
    private Double goalWeight;
    @OneToOne
    private BMI BMI;
    private Long totalCaloriesPerDay;
    @Enumerated(EnumType.STRING)
    private Goal goal;
    @Enumerated(EnumType.STRING)
    private Activity activity;
    private LocalDateTime dateOfCreation;

    public Profile(Long id, EndUser endUser, Integer age, Sex sex, Double height, Double weight, Double goalWeight, BMI BMI, Long totalCaloriesPerDay, Goal goal, Activity activity, LocalDateTime dateOfCreation) {
        this.id = id;
        this.endUser = endUser;
        this.age = age;
        this.sex = sex;
        this.height = height;
        this.weight = weight;
        this.goalWeight = goalWeight;
        this.BMI = BMI;
        this.totalCaloriesPerDay = totalCaloriesPerDay;
        this.goal = goal;
        this.activity = activity;
        this.dateOfCreation = dateOfCreation;
    }

    public Profile() {
    }
}

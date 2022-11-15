package com.example.balancewellspringboot.model;

import com.example.balancewellspringboot.model.identity.EndUser;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
public class LoggedDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDateTime dateForDay;
    private Long targetCalories;
    private Long totalCalories;
    @OneToMany(mappedBy = "loggedDay")
    private List<Meal> allMealsForDay;
    @ManyToOne
    private EndUser endUser;

    public LoggedDay() {

    }

    public LoggedDay(long id, LocalDateTime dateForDay, Long targetCalories, Long totalCalories, List<Meal> allMealsForDay, EndUser endUser) {
        this.id = id;
        this.dateForDay = dateForDay;
        this.targetCalories = targetCalories;
        this.totalCalories = totalCalories;
        this.allMealsForDay = allMealsForDay;
        this.endUser = endUser;
    }
}

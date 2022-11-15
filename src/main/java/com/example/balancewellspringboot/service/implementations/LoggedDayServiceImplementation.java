package com.example.balancewellspringboot.service.implementations;

import com.example.balancewellspringboot.model.*;
import com.example.balancewellspringboot.model.dto.IngredientDTO;
import com.example.balancewellspringboot.model.dto.LoggedDayDTO;
import com.example.balancewellspringboot.model.dto.MealDTO;
import com.example.balancewellspringboot.model.identity.EndUser;
import com.example.balancewellspringboot.repository.LoggedDayRepository;
import com.example.balancewellspringboot.repository.MealRepository;
import com.example.balancewellspringboot.repository.ProfileRepository;
import com.example.balancewellspringboot.service.interfaces.EndUserService;
import com.example.balancewellspringboot.service.interfaces.LoggedDayService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoggedDayServiceImplementation implements LoggedDayService {

    private final LoggedDayRepository loggedDayRepository;
    private final ProfileRepository profileRepository;
    private final EndUserService endUserService;
    private final MealRepository mealRepository;

    public LoggedDayServiceImplementation(LoggedDayRepository loggedDayRepository, ProfileRepository profileRepository, EndUserService endUserService, MealRepository mealRepository) {
        this.loggedDayRepository = loggedDayRepository;
        this.profileRepository = profileRepository;
        this.endUserService = endUserService;
        this.mealRepository = mealRepository;
    }

    @Override
    @Transactional
    public LoggedDay getLoggedDay(String username, LocalDateTime date) {
        EndUser currentUser = (EndUser) endUserService.loadUserByUsername(username);
        Profile currentProfileForUser = profileRepository.getLatestProfileForUsername(username);

        return loggedDayRepository.findByEndUser_UsernameAndDateForDay(username,date)
                .orElseGet(() -> {
                   LoggedDay created = LoggedDay.builder()
                           .dateForDay(date)
                           .endUser(currentUser)
                           .targetCalories(currentProfileForUser.getTotalCaloriesPerDay())
                           .totalCalories(0L)
                           .allMealsForDay(new ArrayList<>())
                           .build()
                           ;
                  loggedDayRepository.save(created);
                  created.setAllMealsForDay(initializeMealList(created));
                  loggedDayRepository.save(created);
                  return created;
                });

    }

    @Transactional
    List<Meal> initializeMealList(LoggedDay loggedDay) {
      List<Meal> newMeals = Arrays.stream(MealEnum.values()).map(m -> Meal.builder()
                .ingredientList(new ArrayList<>())
                .name(m.toString())
                .caloriesInMeal(0.0)
                .loggedDay(loggedDay)
                .build()).collect(Collectors.toList());
      mealRepository.saveAll(newMeals);
      return newMeals;
    }

    @Override
    public LoggedDayDTO getLoggedDayDTO(String username, LocalDateTime date) {
        LoggedDay loggedDay = this.getLoggedDay(username, date);
        return LoggedDayDTO
                .builder()
                .dateForDay(loggedDay.getDateForDay())
                .targetCalories(loggedDay.getTargetCalories())
                .totalCalories(loggedDay.getTotalCalories())
                .mealList(getMealList(loggedDay.getAllMealsForDay()))
                .build();
    }

    private List<MealDTO> getMealList(List<Meal> allMealsForDay) {
        return allMealsForDay.stream().map(m ->
                MealDTO.builder()
                        .name(m.getName())
                        .caloriesInMeal(m.getCaloriesInMeal())
                        .ingredientList(getIngredientList(m.getIngredientList()))
                        .build()
        ).collect(Collectors.toList());
    }

    private List<IngredientDTO> getIngredientList(List<Ingredient> ingredientList) {
        return ingredientList.stream().map(i ->
                IngredientDTO.builder()
                        .name(i.getName())
                        .caloriesInIngredient(i.getCaloriesInIngredient())
                        .measurement(i.getMeasurement())
                        .quantity(i.getQuantity())
                        .build()
        ).collect(Collectors.toList());
    }
}

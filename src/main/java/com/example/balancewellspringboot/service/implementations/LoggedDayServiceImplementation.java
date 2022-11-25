package com.example.balancewellspringboot.service.implementations;

import com.example.balancewellspringboot.model.*;
import com.example.balancewellspringboot.model.dto.CaloriesGoalDTO;
import com.example.balancewellspringboot.model.dto.IngredientDTO;
import com.example.balancewellspringboot.model.dto.LoggedDayDTO;
import com.example.balancewellspringboot.model.dto.MealDTO;
import com.example.balancewellspringboot.model.enums.MealEnum;
import com.example.balancewellspringboot.model.identity.EndUser;
import com.example.balancewellspringboot.repository.LoggedDayRepository;
import com.example.balancewellspringboot.repository.MealRepository;
import com.example.balancewellspringboot.repository.ProfileRepository;
import com.example.balancewellspringboot.service.interfaces.EndUserService;
import com.example.balancewellspringboot.service.interfaces.LoggedDayService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
    public LoggedDay getLoggedDay(String username, LocalDate date) {
        EndUser currentUser = (EndUser) endUserService.loadUserByUsername(username);
        Profile currentProfileForUser = profileRepository.getLatestProfileForUsername(username);

        return loggedDayRepository.findByEndUser_UsernameAndDateForDay(username, date)
                .orElseGet(() -> {
                    LoggedDay created = LoggedDay.builder()
                            .dateForDay(date)
                            .endUser(currentUser)
                            .targetCalories(currentProfileForUser.getTotalCaloriesPerDay())
                            .totalCalories(0L)
                            .allMealsForDay(new ArrayList<>())
                            .build();
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
                .name(m)
                .caloriesInMeal(0.0)
                .loggedDay(loggedDay)
                .build()).collect(Collectors.toList());
        mealRepository.saveAll(newMeals);
        return newMeals;
    }

    @Override
    public LoggedDayDTO getLoggedDayDTO(String username, LocalDate date) {
        LoggedDay loggedDay = this.getLoggedDay(username, date);
        String formattedDate = formatDate(loggedDay.getDateForDay());
        return LoggedDayDTO
                .builder()
                .dateForDay(formattedDate)
                .targetCalories(loggedDay.getTargetCalories())
                .totalCalories(loggedDay.getTotalCalories())
                .mealList(getMealList(loggedDay.getAllMealsForDay()))
                .build();
    }

    @Override
    public LoggedDay saveLoggedDay(LoggedDay loggedDay) {
        return loggedDayRepository.save(loggedDay);
    }

    @Override
    public CaloriesGoalDTO getInfoMessage(LoggedDayDTO loggedDayDTO) {
        if(((loggedDayDTO.getTotalCalories()*1.0 / loggedDayDTO.getTargetCalories()) * 100.0 >= 80)
                && ((loggedDayDTO.getTotalCalories()*1.0 / loggedDayDTO.getTargetCalories()) * 100.0 < 98)) {
            return CaloriesGoalDTO
                    .builder()
                    .status(0)
                    .message("You're on your way to meet your daily target calories! Keep up, you're doing a great job!")
                    .build();
        } else if (((loggedDayDTO.getTotalCalories()*1.0 / loggedDayDTO.getTargetCalories()) * 100.0 >= 98)
                && ((loggedDayDTO.getTotalCalories()*1.0 / loggedDayDTO.getTargetCalories()) * 100.0 < 104)) {
            return CaloriesGoalDTO
                    .builder()
                    .status(1)
                    .message("Great job! You've met your daily calories goal and be proud of yourself!")
                    .build();
        } else if (((loggedDayDTO.getTotalCalories()*1.0 / loggedDayDTO.getTargetCalories()) * 100.0 >= 104)) {
            return CaloriesGoalDTO
                    .builder()
                    .status(2)
                    .message("You're above your daily calories target for the day. Be mindful of your choices, but don't be so hard on yourself.")
                    .build();
        }
        return CaloriesGoalDTO
                .builder()
                .message(null)
                .status(-1)
                .build();
    }

    private String formatDate(LocalDate dateForDay) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd LLL yyyy", Locale.ENGLISH);
        return dateForDay.format(formatter);
    }

    private List<MealDTO> getMealList(List<Meal> allMealsForDay) {
        return allMealsForDay.stream()
                .sorted(Comparator.comparing(meal -> meal.getName().ordinal()))
                .map(m ->
                        MealDTO.builder()
                                .name(StringUtils.capitalize(m.getName().name().toLowerCase()))
                                .caloriesInMeal(m.getCaloriesInMeal().intValue())
                                .ingredientList(getIngredientList(m.getIngredientList()))
                                .build())
                .collect(Collectors.toList());
    }

    private List<IngredientDTO> getIngredientList(List<Ingredient> ingredientList) {
        return ingredientList.stream().map(i ->
                IngredientDTO.builder()
                        .name(i.getName())
                        .foodIdApi(i.getFoodIdApi())
                        .ingredientId(i.getId())
                        .caloriesInIngredient(i.getCaloriesInIngredient())
                        .measurement(i.getMeasurement())
                        .quantity(i.getQuantity())
                        .build()
        ).collect(Collectors.toList());
    }
}

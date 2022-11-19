package com.example.balancewellspringboot.service.implementations;

import com.example.balancewellspringboot.model.Ingredient;
import com.example.balancewellspringboot.model.LoggedDay;
import com.example.balancewellspringboot.model.Meal;
import com.example.balancewellspringboot.model.dto.FoodDetailDTO;
import com.example.balancewellspringboot.model.dto.edamamApi.dto.*;
import com.example.balancewellspringboot.repository.IngredientRepository;
import com.example.balancewellspringboot.repository.LoggedDayRepository;
import com.example.balancewellspringboot.repository.MealRepository;
import com.example.balancewellspringboot.service.interfaces.FoodService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
public class FoodServiceImplementation implements FoodService {

    private final LoggedDayRepository loggedDayRepository;
    private final IngredientRepository ingredientRepository;
    private final MealRepository mealRepository;

    public FoodServiceImplementation(LoggedDayRepository loggedDayRepository, IngredientRepository ingredientRepository, MealRepository mealRepository) {
        this.loggedDayRepository = loggedDayRepository;
        this.ingredientRepository = ingredientRepository;
        this.mealRepository = mealRepository;
    }

    @Override
    public FoodDetailDTO getFoodDetailDTO(EdamamIngredientDTO edamamIngredientDTO) {
        EdamamFoodDetailDTO foodDetailDTO = edamamIngredientDTO.getParsed().get(0).getFood();
        EdamamNutrientDTO nutrientDTO = foodDetailDTO.getNutrients();

        return FoodDetailDTO.builder()
                .foodId(foodDetailDTO.getFoodId())
                .foodName(edamamIngredientDTO.getText())
                .foodImage(foodDetailDTO.getImage())
                .calories(Double.parseDouble(foodDetailDTO.getNutrients().getENERC_KCAL()))
                .proteinGrams(Double.parseDouble(foodDetailDTO.getNutrients().getPROCNT()))
                .fatGrams(Double.parseDouble(nutrientDTO.getFAT()))
                .carbGrams(Double.parseDouble(nutrientDTO.getCHOCDF()))
                .fiberGrams(Double.parseDouble(nutrientDTO.getFIBTG()))
                .measures(
                        edamamIngredientDTO.getHints().stream()
                                .filter(h -> h.getFood().getFoodId().equalsIgnoreCase(foodDetailDTO.getFoodId()))
                                .findFirst().get().getMeasures().stream().map(EdamamMeasuresDTO::getLabel).collect(Collectors.toList())
                ).build();

    }

    @Override
    public Ingredient createIngredient(EdamamFoodDetailResponseDTO foodDTO, String date, String currentUser, String meal) {
        EdamamParsedDTO parsed = foodDTO.getIngredients().get(0).getParsed().get(0);

        LoggedDay loggedDay = loggedDayRepository.findByEndUser_UsernameAndDateForDay(currentUser, LocalDate.parse(date)).orElseThrow();
        Meal mealForIngredient = loggedDay.getAllMealsForDay().stream().filter(m -> m.getName().equalsIgnoreCase(meal)).findFirst().get();

        Ingredient ingredient = Ingredient
                .builder()
                .name(parsed.getFood())
                .foodIdApi(parsed.getFoodId())
                .quantity(parsed.getQuantity())
                .measurement(parsed.getMeasure())
                .caloriesInIngredient((double) foodDTO.getCalories())
                .meal(mealForIngredient)
                .build();


        Ingredient savedIngredient = ingredientRepository.save(ingredient);

        mealForIngredient.getIngredientList().add(savedIngredient);
        mealForIngredient.updateCaloriesInMeal();
        mealRepository.save(mealForIngredient);

        loggedDay.updateTotalCalories();
        loggedDayRepository.save(loggedDay);

        return savedIngredient;
    }
}

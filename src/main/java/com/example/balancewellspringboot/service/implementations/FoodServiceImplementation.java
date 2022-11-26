package com.example.balancewellspringboot.service.implementations;

import com.example.balancewellspringboot.model.Ingredient;
import com.example.balancewellspringboot.model.LoggedDay;
import com.example.balancewellspringboot.model.Meal;
import com.example.balancewellspringboot.model.Recipe;
import com.example.balancewellspringboot.model.dto.AddFoodDTO;
import com.example.balancewellspringboot.model.dto.FoodDetailDTO;
import com.example.balancewellspringboot.model.dto.IngredientDTO;
import com.example.balancewellspringboot.model.dto.RecipeAddToMealRequestDTO;
import com.example.balancewellspringboot.model.dto.edamamApi.dto.request.EdamamFoodQuantityMeasureDTO;
import com.example.balancewellspringboot.model.dto.edamamApi.dto.request.EdamamIngredientDataDTO;
import com.example.balancewellspringboot.model.dto.edamamApi.dto.response.*;
import com.example.balancewellspringboot.model.exceptions.RecipeNotFoundException;
import com.example.balancewellspringboot.repository.IngredientRepository;
import com.example.balancewellspringboot.repository.MealRepository;
import com.example.balancewellspringboot.repository.RecipeRepository;
import com.example.balancewellspringboot.service.interfaces.FoodService;
import com.example.balancewellspringboot.service.interfaces.LoggedDayService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.httpclient.util.URIUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodServiceImplementation implements FoodService {

    private final IngredientRepository ingredientRepository;
    private final MealRepository mealRepository;
    private final LoggedDayService loggedDayService;
    private final RecipeRepository recipeRepository;
    private static final Gson gson = new GsonBuilder().create();

    public FoodServiceImplementation(IngredientRepository ingredientRepository, MealRepository mealRepository, LoggedDayService loggedDayService, RecipeRepository recipeRepository) {
        this.ingredientRepository = ingredientRepository;
        this.mealRepository = mealRepository;
        this.loggedDayService = loggedDayService;
        this.recipeRepository = recipeRepository;
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
                                .findFirst().orElseThrow().getMeasures().stream().map(EdamamMeasuresDTO::getLabel).collect(Collectors.toList())
                ).build();

    }

    @Override
    @Transactional
    public Ingredient createIngredient(EdamamFoodDetailResponseDTO foodDTO, String foodName, String date, String currentUser, String meal) {
        EdamamParsedDTO parsed = foodDTO.getIngredients().get(0).getParsed().get(0);

        LoggedDay loggedDay = this.getLoggedDay(currentUser, LocalDate.parse(date));
        Meal mealForIngredient = getMeal(loggedDay, meal);

        Ingredient ingredient = Ingredient
                .builder()
                .name(foodName)
                .foodIdApi(parsed.getFoodId())
                .quantity(parsed.getQuantity())
                .measurement(parsed.getMeasure())
                .caloriesInIngredient((double) foodDTO.getCalories())
                .meal(mealForIngredient)
                .build();

        Ingredient savedIngredient = ingredientRepository.save(ingredient);

        this.updateCaloriesInMealAndLoggedDay(mealForIngredient, loggedDay);

        return savedIngredient;
    }

    @Override
    public Ingredient editIngredient(EdamamFoodDetailResponseDTO foodDTO, String date, String currentUser, String meal, String ingrId) {
        EdamamParsedDTO parsed = foodDTO.getIngredients().get(0).getParsed().get(0);

        LoggedDay currentLoggedDay = this.getLoggedDay(currentUser, LocalDate.parse(date));
        Meal mealForIngredient = getMeal(currentLoggedDay, meal);

        Ingredient ingredientToEdit = currentLoggedDay.getAllMealsForDay()
                .stream()
                .filter(m -> m.getName().name().equalsIgnoreCase(meal))
                .findFirst()
                .orElseThrow()
                .getIngredientList()
                .stream().filter(i -> i.getId().equals(Long.valueOf(ingrId)))
                .findFirst()
                .orElseThrow();

        ingredientToEdit.setCaloriesInIngredient((double) foodDTO.getCalories());
        ingredientToEdit.setMeasurement(parsed.getMeasure());
        ingredientToEdit.setQuantity(parsed.getQuantity());

        Ingredient savedIngredient = ingredientRepository.save(ingredientToEdit);

        mealForIngredient.getIngredientList().removeIf(i -> i.getId().equals(ingredientToEdit.getId()));

        mealForIngredient.getIngredientList().add(savedIngredient);

        this.updateCaloriesInMealAndLoggedDay(mealForIngredient, currentLoggedDay);

        return savedIngredient;
    }

    @Override
    public List<String> searchForIngredientsFromSearchField(String searchInput, String date, String meal) throws IOException, InterruptedException {
        String uri = URIUtil.encodeQuery("https://api.edamam.com/auto-complete?app_id=64af34d6&app_key=14c978ef5027cb9f2de6dcb328b7e4b6&q=" + searchInput + "&limit=10");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("Accept", "application/json")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        Type foodListType = new TypeToken<ArrayList<String>>() {
        }.getType();

        return gson.fromJson(response.body(), foodListType);
    }

    @Override
    public EdamamIngredientDTO getDetailsDTOForSelectedIngredient(String date, String meal, String foodName) throws IOException, InterruptedException {
        String uri = URIUtil.encodeQuery("https://api.edamam.com/api/food-database/v2/parser?app_id=64af34d6&app_key=14c978ef5027cb9f2de6dcb328b7e4b6&ingr=" + foodName + "&nutrition-type=cooking");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("Accept", "application/json")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        return gson.fromJson(response.body(), EdamamIngredientDTO.class);
    }

    @Override
    public EdamamFoodDetailResponseDTO saveSelectedIngredient(String date, String meal, String foodId, AddFoodDTO addFoodDTO) throws IOException, InterruptedException {
        String uri = URIUtil.encodeQuery("https://api.edamam.com/api/food-database/v2/nutrients?app_id=64af34d6&app_key=14c978ef5027cb9f2de6dcb328b7e4b6");

        EdamamIngredientDataDTO ingrData = EdamamIngredientDataDTO
                .builder()
                .foodId(foodId)
                .measureURI(addFoodDTO.getIngredientMeasurements())
                .quantity(addFoodDTO.getIngredientQuantities())
                .build();
        EdamamFoodQuantityMeasureDTO dto = EdamamFoodQuantityMeasureDTO
                .builder()
                .ingredients(List.of(ingrData))
                .build();


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString(gson.toJson(dto)))
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        return gson.fromJson(response.body(), EdamamFoodDetailResponseDTO.class);
    }

    @Override
    public IngredientDTO getIngredientDTO(String currentUser, LocalDate date, String meal, String ingrId) {
        LoggedDay currentLoggedDay = this.getLoggedDay(currentUser, date);
        Ingredient ingredient = currentLoggedDay.getAllMealsForDay()
                .stream()
                .filter(m -> m.getName().name().equalsIgnoreCase(meal))
                .findFirst()
                .orElseThrow()
                .getIngredientList()
                .stream().filter(i -> i.getId().equals(Long.valueOf(ingrId)))
                .findFirst()
                .orElseThrow();

        return IngredientDTO
                .builder()
                .name(ingredient.getName())
                .ingredientId(ingredient.getId())
                .foodIdApi(ingredient.getFoodIdApi())
                .measurement(ingredient.getMeasurement())
                .caloriesInIngredient(ingredient.getCaloriesInIngredient())
                .quantity(ingredient.getQuantity())
                .build();
    }

    @Override
    public void deleteIngredientById(String ingrId, String currentUser, LocalDate date, String meal) {
        LoggedDay currentLoggedDay = this.getLoggedDay(currentUser, date);
        Meal mealForIngredient = getMeal(currentLoggedDay, meal);
        ingredientRepository.deleteById(Long.valueOf(ingrId));

        this.updateCaloriesInMealAndLoggedDay(mealForIngredient, currentLoggedDay);

    }

    @Override
    public Ingredient addRecipeToMeal(RecipeAddToMealRequestDTO recipeDTO, Long recipeId, String currentUser) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new RecipeNotFoundException(recipeId));

        LoggedDay loggedDay = this.getLoggedDay(currentUser, LocalDate.parse(recipeDTO.getSelectedDate()));
        Meal mealForIngredient = getMeal(loggedDay, recipeDTO.getMeal());

        Ingredient ingredient = Ingredient
                .builder()
                .name(recipe.getTitle())
                .foodIdApi(null)
                .quantity(1.0)
                .measurement("serving")
                .caloriesInIngredient(recipeDTO.getCaloriesRecipe())
                .meal(mealForIngredient)
                .build();

        Ingredient savedIngredient = ingredientRepository.save(ingredient);

        this.updateCaloriesInMealAndLoggedDay(mealForIngredient, loggedDay);

        return savedIngredient;
    }

    private static Meal getMeal(LoggedDay loggedDay, String meal) {
        return loggedDay.getAllMealsForDay().stream().filter(m -> m.getName().name().equalsIgnoreCase(meal)).findFirst().orElseThrow();

    }

    private LoggedDay getLoggedDay(String currentUser, LocalDate selectedDate) {
        return loggedDayService.getLoggedDay(currentUser, selectedDate);
    }

    private void updateCaloriesInMealAndLoggedDay(Meal meal, LoggedDay loggedDay) {
        meal.updateCaloriesInMeal();
        mealRepository.save(meal);

        loggedDay.updateTotalCalories();
        loggedDayService.saveLoggedDay(loggedDay);
    }

}

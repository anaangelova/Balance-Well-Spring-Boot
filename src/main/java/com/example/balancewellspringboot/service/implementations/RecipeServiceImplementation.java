package com.example.balancewellspringboot.service.implementations;

import com.example.balancewellspringboot.model.*;
import com.example.balancewellspringboot.model.dto.RecipeAddToMealDTO;
import com.example.balancewellspringboot.model.dto.RecipeDTO;
import com.example.balancewellspringboot.model.exceptions.RecipeNotFoundException;
import com.example.balancewellspringboot.model.identity.EndUser;
import com.example.balancewellspringboot.repository.*;
import com.example.balancewellspringboot.service.interfaces.EndUserService;
import com.example.balancewellspringboot.service.interfaces.RecipeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecipeServiceImplementation implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final EndUserRepository endUserRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final RecipeInstructionRepository recipeInstructionRepository;
    private final RecipeImageRepository recipeImageRepository;
    private final EndUserService endUserService;

    public RecipeServiceImplementation(RecipeRepository recipeRepository, EndUserRepository endUserRepository, RecipeIngredientRepository recipeIngredientRepository, RecipeInstructionRepository recipeInstructionRepository, RecipeImageRepository recipeImageRepository, EndUserService endUserService) {
        this.recipeRepository = recipeRepository;
        this.endUserRepository = endUserRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.recipeInstructionRepository = recipeInstructionRepository;
        this.recipeImageRepository = recipeImageRepository;
        this.endUserService = endUserService;
    }

    @Override
    public Recipe findById(Long id) {
        return recipeRepository.findById(id).orElseThrow(() -> new RecipeNotFoundException(id));
    }

    @Override
    public List<Recipe> findAllRecipesMostRecent() {
        return recipeRepository.findMostRecentRecipes();
    }

    @Override
    public List<Recipe> findAllRecipesTrending() {
        List<Recipe> all = recipeRepository.findAll();
        Collections.shuffle(all);
        if (all.size() > 4)
            return all.subList(0, 4);
        else return all;
    }

    @Override
    public List<Recipe> findAllRecipesByUser(String username) {
        return recipeRepository.findAllByAuthor_Username(username);
    }

    @Transactional
    @Override
    public Optional<Recipe> save(RecipeDTO recipeDTO, List<String> imagesNames) throws IOException {
        Recipe recipeToAdd = new Recipe();
        recipeToAdd.setMeal(Arrays.stream(MealEnum.values()).filter(m -> m.name().equalsIgnoreCase(recipeDTO.getMeal())).findFirst().get());

        EndUser author = (EndUser) endUserService.loadUserByUsername(recipeDTO.getAuthor());
        recipeToAdd.setAuthor(author);

        recipeToAdd.setTitle(recipeDTO.getTitle());
        recipeToAdd.setDescription(recipeDTO.getDescription());

        recipeToAdd.setPrepInMins(recipeDTO.getPrepH() * 60 + recipeDTO.getPrepM());
        recipeToAdd.setCookInMins(recipeDTO.getCookH() * 60 + recipeDTO.getCookM());
        recipeToAdd.setDateCreated(LocalDateTime.now());
        recipeToAdd.setCaloriesInRecipe(recipeDTO.getCaloriesRecipe());

        //One to many
        List<RecipeIngredient> ingredients = getIngredients(recipeDTO, recipeToAdd);
        recipeIngredientRepository.saveAll(ingredients);
        recipeToAdd.setRecipeIngredientList(ingredients);
        Recipe addedRecipe = recipeRepository.save(recipeToAdd);

        //One to many
        List<RecipeInstruction> instructionsToAdd = getInstructions(recipeDTO, addedRecipe);
        recipeInstructionRepository.saveAll(instructionsToAdd);

        addedRecipe.setInstructions(instructionsToAdd); // ???

        //images
        List<RecipeImage> imagesToAdd = new ArrayList<>();
        for (String m : imagesNames) {
            imagesToAdd.add(new RecipeImage(m, addedRecipe));
        }
        recipeImageRepository.saveAll(imagesToAdd);
        addedRecipe.setImages(imagesToAdd);

        return Optional.of(recipeRepository.save(addedRecipe));
    }

    @Override
    public void deleteById(Long id) {
        recipeRepository.deleteById(id);
    }

    @Override
    public Optional<Recipe> editRecipe(RecipeDTO recipeDTO, Long recipeId) {
        Recipe recipeToAdd = this.findById(recipeId);
        recipeToAdd.setMeal(Arrays.stream(MealEnum.values()).filter(m -> m.name().equalsIgnoreCase(recipeDTO.getMeal())).findFirst().get());


        recipeToAdd.setTitle(recipeDTO.getTitle());
        recipeToAdd.setDescription(recipeDTO.getDescription());
        recipeToAdd.setPrepInMins(recipeDTO.getPrepH() * 60 + recipeDTO.getPrepM());
        recipeToAdd.setCookInMins(recipeDTO.getCookH() * 60 + recipeDTO.getCookM());
        recipeToAdd.setCaloriesInRecipe(recipeDTO.getCaloriesRecipe());

        //One to many
        List<RecipeIngredient> ingredients = getIngredients(recipeDTO, recipeToAdd);
        List<RecipeIngredient> tmp = recipeToAdd.getRecipeIngredientList();
        recipeIngredientRepository.deleteAll(tmp);

        recipeIngredientRepository.saveAll(ingredients);
        recipeToAdd.setRecipeIngredientList(ingredients);

        Recipe addedRecipe = recipeRepository.save(recipeToAdd);

        //One to many
        List<RecipeInstruction> instructionsToAdd = getInstructions(recipeDTO, addedRecipe);
        List<RecipeInstruction> tmp2 = recipeToAdd.getInstructions();
        recipeInstructionRepository.deleteAll(tmp2);
        recipeInstructionRepository.saveAll(instructionsToAdd);
        addedRecipe.setInstructions(instructionsToAdd);

        recipeRepository.save(addedRecipe);
        return Optional.of(addedRecipe);
    }

    @Override
    public List<Recipe> findBySearch(String search) {
        //TODO
        return null;
    }

    @Override
    public RecipeAddToMealDTO getRecipeAddToMealDTO(Long id) {
        Recipe recipeToAdd = this.findById(id);

        return RecipeAddToMealDTO
                .builder()
                .id(recipeToAdd.getId())
                .title(recipeToAdd.getTitle())
                .image(recipeToAdd.getImages().get(0).getTitle())
                .caloriesInRecipe(recipeToAdd.getCaloriesInRecipe())
                .ingredientList(recipeToAdd.getRecipeIngredientList().stream().map(i -> i.getFormatted()).collect(Collectors.toList()))
                .build()
                ;
    }

    private List<RecipeIngredient> getIngredients(RecipeDTO recipeDTO, Recipe recipeToAdd) {

        List<RecipeIngredient> ingredients = new ArrayList<>();
        List<String> names = recipeDTO.getIngredientNames();
        List<Double> quantities = recipeDTO.getIngredientQuantities();
        List<RecipeIngredientMeasurement> measurements = recipeDTO.getIngredientMeasurements();
        for (int i = 0; i < names.size(); i++) {
            ingredients.add(new RecipeIngredient(names.get(i), quantities.get(i), measurements.get(i),recipeToAdd));
        }
        return ingredients;
    }

    private List<RecipeInstruction> getInstructions(RecipeDTO recipeDTO, Recipe addedRecipe) {

        return recipeDTO.getInstructions().stream().map(i -> new RecipeInstruction(i, addedRecipe)).collect(Collectors.toList());
    }

}

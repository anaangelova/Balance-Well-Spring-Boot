package com.example.balancewellspringboot.web;

import com.example.balancewellspringboot.model.enums.MealEnum;
import com.example.balancewellspringboot.model.Recipe;
import com.example.balancewellspringboot.model.enums.RecipeIngredientMeasurement;
import com.example.balancewellspringboot.model.dto.*;
import com.example.balancewellspringboot.model.identity.EndUser;
import com.example.balancewellspringboot.service.interfaces.EndUserService;
import com.example.balancewellspringboot.service.interfaces.FoodService;
import com.example.balancewellspringboot.service.interfaces.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/recipes")
public class RecipeController {

    private final EndUserService endUserService;
    private final RecipeService recipeService;
    private final FoodService foodService;

    public RecipeController(EndUserService endUserService, RecipeService recipeService, FoodService foodService) {
        this.endUserService = endUserService;
        this.recipeService = recipeService;
        this.foodService = foodService;
    }

    @GetMapping()
    public String getAllRecipes(Model model) {

        List<Recipe> mostRecent = recipeService.findAllRecipesMostRecent();
        List<Recipe> trending = recipeService.findAllRecipesTrending();
        model.addAttribute("recents", mostRecent);
        model.addAttribute("trendings", trending);

        return "recipes";
    }

    @GetMapping("/addRecipe")
    public String addRecipe(Model model) {
        ContentDTO toShow = this.getContent();
        model.addAttribute("dto", toShow);
        List<String> allMeasurements = Arrays.stream(RecipeIngredientMeasurement.values()).map(m -> m.name()).collect(Collectors.toList());
        model.addAttribute("measurements", allMeasurements);
        return "add-recipe";
    }

    @PostMapping("/addRecipe")
    public String addRecipePost(RecipeDTO recipeAdded, @RequestPart List<MultipartFile> images, HttpServletRequest request) throws IOException {

        List<String> imagesNames = Collections.singletonList(this.saveImage(images.get(0)));
        recipeService.save(recipeAdded, imagesNames);
        return "redirect:/recipes/myRecipes/" + request.getRemoteUser();
    }

    @GetMapping("/details/{id}")
    public String getDetailsForRecipe(@PathVariable Long id, Model model) {

        RecipeDetailsDTO detailsDTO = this.getDetailsDTO(id);
        model.addAttribute("detailsDTO", detailsDTO);
        return "recipeDetails";

    }

    @GetMapping("/search")
    public String searchForResults(@RequestParam String searchInput, Model model) {

        List<Recipe> recipes = recipeService.findBySearch(searchInput);
        model.addAttribute("recipes", recipes);
        model.addAttribute("val", searchInput);
        return "recipes-category";
    }

    @GetMapping("/myRecipes/{id}")
    public String getUsersRecipes(@PathVariable String id, Model model, HttpServletRequest request) {
        List<Recipe> recipesByUser = recipeService.findAllRecipesByUser(id);
        model.addAttribute("recipes", recipesByUser);
        EndUser author = (EndUser) endUserService.loadUserByUsername(request.getRemoteUser());
        model.addAttribute("author", author);

        return "my-recipes";
    }

    @GetMapping("/edit/{id}")
    public String editRecipe(@PathVariable Long id, Model model, HttpServletRequest request) {
        Recipe toEdit = recipeService.findById(id);
        if (this.isAuthor(toEdit, request.getRemoteUser())) {
            ContentDTO contentDTO = this.getContent();
            EditDTO editDTO = this.getEditDTO(toEdit);
            model.addAttribute("contentDTO", contentDTO);
            model.addAttribute("editDTO", editDTO);
            List<String> allMeasurements = Arrays.stream(RecipeIngredientMeasurement.values()).map(m -> m.name()).collect(Collectors.toList());
            model.addAttribute("measurements", allMeasurements);
            return "edit";

        } else return "redirect:/recipes";
    }


    @PostMapping("/editRecipe")
    public String editRecipePost(RecipeDTO recipeEdited, Long recipeId, String author) {
        recipeService.editRecipe(recipeEdited, recipeId);
        return "redirect:/recipes/myRecipes/" + author;
    }

    @DeleteMapping("/delete/{id}")
    public String deleteRecipe(@PathVariable Long id, HttpServletRequest request) {
        this.recipeService.deleteById(id);
        return "redirect:/recipes/myRecipes/" + request.getRemoteUser();
    }

    @GetMapping("/byAuthor/{author}")
    public String getByAuthor(@PathVariable String author, Model model){
        List<Recipe> recipesList=recipeService.findAllRecipesByUser(author);
        model.addAttribute("recipes",recipesList);
        model.addAttribute("val",author);
        model.addAttribute("forAuthor",true);
        return "recipes-category";

    }

    @GetMapping("/addToMeal/{id}")
    public String addRecipeToMeal(@PathVariable(value = "id") String recipeId, Model model) {
        RecipeAddToMealDTO recipeDTO = recipeService.getRecipeAddToMealDTO(Long.valueOf(recipeId));
        model.addAttribute("recipeDTO", recipeDTO);
        model.addAttribute("mealList", MealEnum.values());
        return "add-recipe-to-meal";

    }

    @PostMapping("/addToMeal")
    public String addRecipeToMealPost(RecipeAddToMealRequestDTO recipeDTO, HttpServletRequest httpServletRequest) {
        foodService.addRecipeToMeal(recipeDTO, (long) recipeDTO.getRecipeId(), httpServletRequest.getRemoteUser());

        return "redirect:/home";
    }

    private ContentDTO getContent() {
        List<MealEnum> allMeals = List.of(MealEnum.values());
        List<String> allMeasurements = Arrays.stream(RecipeIngredientMeasurement.values()).map(m -> m.name()).collect(Collectors.toList());
        return new ContentDTO(allMeals, allMeasurements);
    }

    private String saveImage(MultipartFile img) throws IOException {
        String imgName = img.getOriginalFilename();
        File fileToUpload = new File("recipes_images/" + imgName);
        fileToUpload.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(fileToUpload);
        fileOutputStream.write(img.getBytes());
        fileOutputStream.close();
        return img.getOriginalFilename();
    }

    private RecipeDetailsDTO getDetailsDTO(Long id) {
        Recipe toShow = recipeService.findById(id);
        List<String> ingredients = toShow.getRecipeIngredientList().stream().map(i -> i.getFormatted()).collect(Collectors.toList());
        List<String> instructions = toShow.getInstructions().stream().map(instr -> instr.getDescription()).collect(Collectors.toList());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formatDateTime = toShow.getDateCreated().format(formatter);


        return new RecipeDetailsDTO(instructions, ingredients, formatDateTime, toShow);

    }

    private boolean isAuthor(Recipe toEdit, String author) {
        return author.equals(toEdit.getAuthor().getUsername());
    }

    private EditDTO getEditDTO(Recipe toEdit) {
        int prepH = (int) (toEdit.getPrepInMins() / 60);
        int cookH = (int) (toEdit.getCookInMins() / 60);
        return new EditDTO(toEdit, prepH, cookH);
    }
}

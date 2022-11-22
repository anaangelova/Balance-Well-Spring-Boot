package com.example.balancewellspringboot.web;

import com.example.balancewellspringboot.model.dto.AddFoodDTO;
import com.example.balancewellspringboot.model.dto.FoodDetailDTO;
import com.example.balancewellspringboot.model.dto.IngredientDTO;
import com.example.balancewellspringboot.model.dto.edamamApi.dto.EdamamFoodDetailResponseDTO;
import com.example.balancewellspringboot.model.dto.edamamApi.dto.EdamamIngredientDTO;
import com.example.balancewellspringboot.service.interfaces.FoodService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;


@Controller
@RequestMapping("/food")
public class FoodController {

    private final FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @GetMapping("/addFood/{date}/{meal}")
    public String getHomePage(@PathVariable String date, @PathVariable String meal, Model model){
        model.addAttribute("meal",meal);
        model.addAttribute("date",date);
        return "add-food";
    }

    @GetMapping("/search/{date}/{meal}")
    public String searchForResults(@RequestParam String searchInput, @PathVariable String date, @PathVariable String meal, Model model) throws IOException, InterruptedException {
        List<String> foodSearchList = foodService.searchForIngredientsFromSearchField(searchInput, date, meal);

        model.addAttribute("foodSearchList", foodSearchList);
        model.addAttribute("meal",meal);
        model.addAttribute("date",date);

        return "add-food";
    }

    @GetMapping("/getDetails/{date}/{meal}/{food}")
    public String getDetailsForFood(@PathVariable String date, @PathVariable String meal, @PathVariable(name = "food") String foodName, Model model) throws IOException, InterruptedException {
        EdamamIngredientDTO edamamIngredientDTO = foodService.getDetailsDTOForSelectedIngredient(date, meal, foodName);
        FoodDetailDTO foodDetailDTO = foodService.getFoodDetailDTO(edamamIngredientDTO);

        model.addAttribute("foodDetail", foodDetailDTO);
        model.addAttribute("date", date);
        model.addAttribute("meal", meal);

        return "food-detail";
    }

    @PostMapping("/saveFood/{date}/{meal}/{foodId}/{foodName}")
    public String saveFood(@PathVariable String date, @PathVariable String meal,@PathVariable String foodId, @PathVariable String foodName, AddFoodDTO addFoodDTO, HttpServletRequest httpServletRequest) throws IOException, InterruptedException {
        EdamamFoodDetailResponseDTO edamamFoodDetailResponseDTO = foodService.saveSelectedIngredient(date, meal, foodId, addFoodDTO);
        foodService.createIngredient(edamamFoodDetailResponseDTO, foodName, date, httpServletRequest.getRemoteUser(), meal);

        return "redirect:/home";
    }

    @GetMapping("/edit/{date}/{meal}/{foodName}/{ingrId}")
    public String getEditFood(@PathVariable String date, @PathVariable String meal, @PathVariable String foodName, @PathVariable String ingrId, Model model, HttpServletRequest httpServletRequest) throws IOException, InterruptedException {
        IngredientDTO ingredientDTO = foodService.getIngredientDTO(httpServletRequest.getRemoteUser(), LocalDate.parse(date), meal, ingrId);
        EdamamIngredientDTO edamamIngredientDTO = foodService.getDetailsDTOForSelectedIngredient(date, meal, foodName);
        FoodDetailDTO foodDetailDTO = foodService.getFoodDetailDTO(edamamIngredientDTO);

        model.addAttribute("ingredientInfo", ingredientDTO);
        model.addAttribute("foodDetail", foodDetailDTO);
        model.addAttribute("date", date);
        model.addAttribute("meal", meal);

        return "edit-food";

    }

    @PostMapping("/editFood/{date}/{meal}/{foodId}/{ingrId}")
    public String editFoodPostMapping(@PathVariable String date, @PathVariable String meal,@PathVariable String foodId, @PathVariable String ingrId, AddFoodDTO addFoodDTO, HttpServletRequest httpServletRequest) throws IOException, InterruptedException {
        EdamamFoodDetailResponseDTO edamamFoodDetailResponseDTO = foodService.saveSelectedIngredient(date, meal, foodId, addFoodDTO);
        String currentUser = httpServletRequest.getRemoteUser();

        foodService.editIngredient(edamamFoodDetailResponseDTO, date, currentUser, meal, ingrId);

        return "redirect:/home";
    }

    @DeleteMapping("/delete/{ingrId}/{date}/{meal}")
    public String deleteIngredient(@PathVariable String ingrId, @PathVariable String date, @PathVariable String meal, HttpServletRequest httpServletRequest) {
        foodService.deleteIngredientById(ingrId,httpServletRequest.getRemoteUser(), LocalDate.parse(date), meal);
        return "redirect:/home";
    }
}

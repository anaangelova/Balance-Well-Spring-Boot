package com.example.balancewellspringboot.web;

import com.example.balancewellspringboot.model.Ingredient;
import com.example.balancewellspringboot.model.dto.AddFoodDTO;
import com.example.balancewellspringboot.model.dto.FoodDetailDTO;
import com.example.balancewellspringboot.model.dto.edamamApi.dto.EdamamFoodDetailResponseDTO;
import com.example.balancewellspringboot.model.dto.edamamApi.dto.EdamamFoodQuantityMeasureDTO;
import com.example.balancewellspringboot.model.dto.edamamApi.dto.EdamamIngredientDTO;
import com.example.balancewellspringboot.model.dto.edamamApi.dto.EdamamIngredientDataDTO;
import com.example.balancewellspringboot.service.interfaces.FoodService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.httpclient.util.URIUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
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
        String uri = URIUtil.encodeQuery("https://api.edamam.com/auto-complete?app_id=64af34d6&app_key=14c978ef5027cb9f2de6dcb328b7e4b6&q=" + searchInput  + "&limit=10");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("Accept", "application/json")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        Gson gson = new GsonBuilder().create();
        Type foodListType = new TypeToken<ArrayList<String>>(){}.getType();
        List<String> foodSearchList = gson.fromJson(response.body(), foodListType);

        model.addAttribute("foodSearchList", foodSearchList);
        model.addAttribute("meal",meal);
        model.addAttribute("date",date);
        return "add-food";
    }

    @GetMapping("/getDetails/{date}/{meal}/{food}")
    public String getDetailsForFood(@PathVariable String date, @PathVariable String meal, @PathVariable String food, Model model) throws IOException, InterruptedException {
        String uri = URIUtil.encodeQuery("https://api.edamam.com/api/food-database/v2/parser?app_id=64af34d6&app_key=14c978ef5027cb9f2de6dcb328b7e4b6&ingr="+ food + "&nutrition-type=cooking");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("Accept", "application/json")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        Gson gson = new GsonBuilder().create();
        EdamamIngredientDTO edamamIngredientDTO = gson.fromJson(response.body(), EdamamIngredientDTO.class);
        FoodDetailDTO foodDetailDTO = foodService.getFoodDetailDTO(edamamIngredientDTO);

        model.addAttribute("foodDetail", foodDetailDTO);
        model.addAttribute("date", date);
        model.addAttribute("meal", meal);
        return "food-detail";
    }

    @PostMapping("/saveFood/{date}/{meal}/{foodId}")
    public String saveFood(@PathVariable String date, @PathVariable String meal,@PathVariable String foodId, AddFoodDTO addFoodDTO, HttpServletRequest httpServletRequest, Model model) throws IOException, InterruptedException {
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
        Gson gson = new GsonBuilder().create();


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString(gson.toJson(dto)))
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        EdamamFoodDetailResponseDTO edamamFoodDetailResponseDTO = gson.fromJson(response.body(), EdamamFoodDetailResponseDTO.class);

        String currentUser = httpServletRequest.getRemoteUser();

        foodService.createIngredient(edamamFoodDetailResponseDTO, date, currentUser, meal);

        return "redirect:/home";
    }
}

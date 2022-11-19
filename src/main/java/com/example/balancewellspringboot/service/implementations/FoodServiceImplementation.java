package com.example.balancewellspringboot.service.implementations;

import com.example.balancewellspringboot.model.dto.FoodDetailDTO;
import com.example.balancewellspringboot.model.dto.edamamApi.dto.EdamamFoodDetailDTO;
import com.example.balancewellspringboot.model.dto.edamamApi.dto.EdamamIngredientDTO;
import com.example.balancewellspringboot.model.dto.edamamApi.dto.EdamamMeasuresDTO;
import com.example.balancewellspringboot.model.dto.edamamApi.dto.EdamamNutrientDTO;
import com.example.balancewellspringboot.service.interfaces.FoodService;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class FoodServiceImplementation implements FoodService {

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
}

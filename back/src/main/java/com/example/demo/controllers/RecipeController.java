package com.example.demo.controllers;

import com.example.demo.dao.RecipeRepository;
import com.example.demo.models.Recipe;
import com.example.demo.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RecipeController {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeService recipeService;

    @PostMapping("/recipes")
    public Recipe createRecipe(@RequestBody Recipe recipe) {
        System.out.println(recipe);
        return recipeRepository.save(recipe);
    }

    @GetMapping("/recipes")
    public List<Recipe> getRecipes() {
        List<Recipe> recipes = new ArrayList<>();

        Recipe recipe = new Recipe();
        recipe.setName("Chicken and Pineapple Salad With Curry Mayonnaise");
        recipe.setDescription("Pasta with tomato sauce");
        recipe.setCookTime("30 minutes");
        recipe.setRating("4.5");
        recipe.setCalories("500");
        recipe.setKeywords("pasta, tomato, sauce");
        recipe.setRecipeCategory("Main Course");
        recipe.setImageUrl("https://img.sndimg.com/food/image/upload/w_555,h_416,c_fit,fl_progressive,q_95/v1/img/submissions/recipe/1802649943/NwpJL4MLRrSWd0jwtfsi_EC_Nutella%20cinnamon%20muffins.jpg");
        recipe.setCookingInstructions("1. Boil water\n2. Add pasta\n3. Cook for 10 minutes\n4. Add tomato sauce\n5. Cook for 5 minutes\n6. Serve hot");
        recipe.setIngredientsParts("pasta, tomato sauce");
        recipe.setIngredientsQuantities("200g, 100g");
        recipes.add(recipe);

        // sleep for 5 seconds

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return recipes;

//        return recipeRepository.findAll();
    }

//    @GetMapping
//    public ResponseEntity<List<Recipe>> getPaginatedRecipes(
//            @RequestParam(defaultValue = "0") int offset,
//            @RequestParam(defaultValue = "10") int limit
//    ) {
//        Page<Recipe> page = recipeService.getRecipes(offset, limit);
//        return ResponseEntity.ok(page.getContent());
//    }
}

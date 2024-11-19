package com.example.demo.controllers;

import com.example.demo.dao.RecipeRepository;
import com.example.demo.models.Recipe;
import com.example.demo.services.RecipeService;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RecipeController {

    @Autowired
    private RecipeRepository recipeRepository;

    @PostMapping("/recipes")
    public Recipe createRecipe(@RequestBody Recipe recipe) {
        System.out.println(recipe);
        return recipeRepository.save(recipe);
    }

    @GetMapping("recipes")
    public List<Recipe> getPaginatedRecipes(Pageable pageable) {
        return recipeRepository.findAll(pageable).get().toList();
    }



    // search recipe by name and category
    @GetMapping("/recipes/search")
    public List<Recipe> searchRecipes(@Nullable @RequestParam String name, @Nullable @RequestParam List<String> category, Pageable pageable) {
        System.out.println(category);
        Page<Recipe> recipesPage;
        if (category.size() == 0) {
            System.out.println("category is empty");
            recipesPage = recipeRepository.findByNameContaining(name, pageable);
        } else if (name == null) {
            recipesPage = recipeRepository.findByRecipeCategoryIn(category, pageable);
        } else {
            recipesPage = recipeRepository.findByNameContainingAndRecipeCategoryIn(name, category, pageable);
        }
        return recipesPage.getContent();
    }
}

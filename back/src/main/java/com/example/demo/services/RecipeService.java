package com.example.demo.services;

import com.example.demo.dao.RecipeRepository;
import com.example.demo.models.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

//    public Page<Recipe> getRecipes(int offset, int limit) {
//        Pageable pageable = PageRequest.of(offset, limit, Sort.by("id").ascending());
//        return recipeRepository.findAllRecipes(pageable);
//    }
}

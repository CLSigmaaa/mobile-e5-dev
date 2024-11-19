package com.example.demo.dao;

import com.example.demo.models.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Page<Recipe> findAll(Pageable pageable);

    Page<Recipe> findByNameContaining(String name, Pageable pageable);

    Page<Recipe> findByRecipeCategoryIn(List<String> categories, Pageable pageable);

    Page<Recipe> findByNameContainingAndRecipeCategoryIn(String name, List<String> categories, Pageable pageable);
}

package com.example.recipe_app.model

data class Recipe(
    val id: Int,
    val name: String,
    var description: String,
    var cookTime: String,
    var rating: Float,
    var calories: Float,
    var keywords: String,
    var recipeCategory: String,
    val imageUrl: String,
    val cookingInstructions: String,
    val ingredients: String
)
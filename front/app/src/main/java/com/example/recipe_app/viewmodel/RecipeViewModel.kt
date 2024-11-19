package com.example.recipe_app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipe_app.model.Category
import com.example.recipe_app.model.Recipe
import com.example.recipe_app.network.RetrofitClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class STATE {
    LOADING,
    SUCCESS,
    FAILED
}

// constant pageSize
const val PAGE_SIZE = 20

class RecipeViewModel: ViewModel() {
    var recipesResponse: List<Recipe> by mutableStateOf(listOf())
    var categoriesResponse: List<Category> by mutableStateOf(listOf())
    private var lastOffset: Long by mutableLongStateOf(0.toLong())
    private var errorMessage: String by mutableStateOf("")

    var state by mutableStateOf(STATE.LOADING)

    fun getRecipes() {
        viewModelScope.launch {
            state = STATE.LOADING
            val apiService = RetrofitClient.getInstance()
            try{
                val apiResponse = apiService.getRecipes()
                recipesResponse = apiResponse
                lastOffset = recipesResponse.size.toLong()
                state = STATE.SUCCESS
            }catch (e: Exception){
                errorMessage = e.message.toString()
                state = STATE.FAILED
            }
        }
    }

    fun getMoreRecipes() {
        viewModelScope.launch {
            state = STATE.LOADING
            delay(5000)
            val apiService = RetrofitClient.getInstance()
            try{
                val apiResponse = apiService.getMoreRecipes(
                    page = lastOffset.toInt() / PAGE_SIZE
                )
                recipesResponse = recipesResponse.plus(apiResponse)
                lastOffset = recipesResponse.size.toLong()
                state = STATE.SUCCESS
            }catch (e: Exception){
                errorMessage = e.message.toString()
                state = STATE.FAILED
            }
        }
    }

    fun searchRecipes(name: String, category: String) {
        println("searchRecipes: $name, $category")
        viewModelScope.launch {
            state = STATE.LOADING
            val apiService = RetrofitClient.getInstance()
            try{
                val apiResponse = apiService.searchRecipes(
                    name = name,
                    category = category
                )
                recipesResponse = apiResponse
                state = STATE.SUCCESS
            }catch (e: Exception){
                errorMessage = e.message.toString()
                state = STATE.FAILED
            }
        }
    }

    fun getCategories() {
        viewModelScope.launch {
            state = STATE.LOADING
            val apiService = RetrofitClient.getInstance()
            try{
                val apiResponse = apiService.getCategories()
                categoriesResponse = apiResponse
                state = STATE.SUCCESS
            }catch (e: Exception){
                errorMessage = e.message.toString()
                state = STATE.FAILED
            }
        }
    }
}
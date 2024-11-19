package com.example.recipe_app.network

import com.example.recipe_app.model.Category
import com.example.recipe_app.model.Recipe
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    // Define the endpoints
    @GET("recipes")
    suspend fun getRecipes(): List<Recipe>

    @GET("recipes")
    suspend fun getMoreRecipes(
        @Query("page") page: Int,
    ): List<Recipe>

    @GET("recipes/search")
    suspend fun searchRecipes(
        @Query("name") name: String,
        @Query("category") category: String,
    ): List<Recipe>

    @GET("categories")
    suspend fun getCategories(): List<Category>
}

//object RetrofitClient {
//    private const val BASE_URL = "http://10.0.2.2:8080/"
//
//    val retrofit: Retrofit by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
//}
//
//object ApiClient {
//    val apiService: ApiService by lazy {
//        RetrofitClient.retrofit.create(ApiService::class.java)
//    }
//}

class RetrofitClient {
    companion object {
        private var apiService: ApiService?= null
        fun getInstance(): ApiService{
            if (apiService == null) {
                apiService = Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8080/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ApiService::class.java)
            }
            return apiService!!
        }
    }
}
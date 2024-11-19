package com.example.recipe_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

class RecipeDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val recipeName = intent.getStringExtra("recipeName") ?: "Unknown Recipe"
            val recipeImage = intent.getStringExtra("recipeImage") ?: ""
            val recipePrepTime = intent.getStringExtra("recipePrepTime") ?: "Unknown Time"
            val recipeRating = intent.getStringExtra("recipeRating") ?: "N/A"
            val recipeSteps = intent.getStringExtra("recipeSteps") ?: "No steps provided"

            RecipeDetailScreen(
                name = recipeName,
                image = recipeImage,
                prepTime = recipePrepTime,
                rating = recipeRating,
                steps = recipeSteps
            )
        }
    }
}

@Composable
fun RecipeDetailScreen(name: String, image: String, prepTime: String, rating: String, steps: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        AsyncImage(
            model = image,
            contentDescription = "Recipe Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = name,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Prep Time: $prepTime",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = "Rating: $rating",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Steps:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = steps,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

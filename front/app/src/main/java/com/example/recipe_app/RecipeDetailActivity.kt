package com.example.recipe_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.recipe_app.ui.theme.RecipeappTheme
import kotlin.time.Duration

class RecipeDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val recipeName = intent.getStringExtra("recipeName") ?: ""
        val recipeImage = intent.getStringExtra("recipeImage") ?: ""
        val recipePrepTime = intent.getStringExtra("recipePrepTime") ?: ""
        val recipeRating = intent.getStringExtra("recipeRating") ?: ""
        val recipeSteps = intent.getStringExtra("recipeSteps") ?: ""
        val recipeIngredients = intent.getStringExtra("recipeIngredientsParts") ?: ""
        val recipeQuantities = intent.getStringExtra("recipeIngredientsQuantities") ?: ""

        val regex = "'([^']*)'".toRegex()

        // transform the string into a list of ingredients
        val ingredientsList = regex.findAll(recipeIngredients)
            .map { it.groupValues[1] }  // Prend le contenu entre les guillemets
            .filter { it.isNotEmpty() } // Filtre les chaînes vides
            .toList()
        val quantitiesList = recipeQuantities.removeSurrounding("[", "]")
            .split(",")
            .map { it.trim().removeSurrounding("'") }

        val stepsList = regex.findAll(recipeSteps)
            .map { it.groupValues[1] }  // Prend le contenu entre les guillemets
            .filter { it.isNotEmpty() } // Filtre les chaînes vides
            .toList()

        println("recipeSteps: $recipeSteps")
        // print type of recipeSteps
        println("recipeSteps type: ${recipeSteps::class.simpleName}")

        println("stepsList: $stepsList")


        setContent {
            RecipeappTheme {
                RecipeDetailScreen(
                    recipeName = recipeName,
                    recipeImage = recipeImage,
                    prepTime = recipePrepTime,
                    rating = recipeRating,
                    steps = stepsList,
                    ingredientsList = ingredientsList,
                    quantitiesList = quantitiesList,
                    onBackPressed = { finish() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    recipeName: String,
    recipeImage: String,
    prepTime: String,
    rating: String,
    steps: List<String>,
    ingredientsList: List<String>,
    quantitiesList: List<String>,
    onBackPressed: () -> Unit
) {
    val scrollState = rememberScrollState()

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(text = recipeName) },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(padding)
        ) {
            // Recipe Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                AsyncImage(
                    model = recipeImage,
                    contentDescription = "Recipe Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Overlay with recipe info
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(
                            color = Color.Black.copy(alpha = 0.4f)
                        )
                        .padding(16.dp)
                ) {
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Prep Time
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.prep_time_icon),
                                contentDescription = "Prep Time",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = Duration.parseIsoString(prepTime).toString(),
                                color = Color.White,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }

                        // Rating
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.star_rating_icon),
                                contentDescription = "Rating",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = rating,
                                color = Color.White,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }

            // Recipe Details Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Ingredients Section
                Text(
                    text = "Ingredients",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Example ingredients - Replace with actual data
                Card (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        for (i in ingredientsList.indices) {
                            if (i < quantitiesList.size) {
                                Row {
                                    Text(
                                        text = "•",
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(end = 8.dp)
                                    )
                                    Text(
                                        text = quantitiesList[i],
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(end = 8.dp)
                                    )
                                    Text(
                                        text = ingredientsList[i],
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }

                // Steps Section
                Text(
                    text = "Instructions",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        for (i in steps.indices) {
                            Row {
                                Text(
                                    text = "${i + 1}.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Text(
                                    text = steps[i],
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
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

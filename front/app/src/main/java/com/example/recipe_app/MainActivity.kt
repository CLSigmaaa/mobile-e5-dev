package com.example.recipe_app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.recipe_app.ui.theme.RecipeappTheme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.recipe_app.model.Recipe
import com.example.recipe_app.network.ApiClient

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RecipeappTheme {
                HomeScreen()
            }
        }
    }
}

@Composable
fun HomeScreen() {
    val searchQuery = remember { mutableStateOf(TextFieldValue("")) }
    val recipes = remember { mutableStateOf(emptyList<Recipe>()) }
    val isLoading = remember { mutableStateOf(true) }
    val isError = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        try {
            recipes.value = ApiClient.apiService.getRecipes()
            isLoading.value = false
        } catch (e: Exception) {
            isError.value = true
            isLoading.value = false
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header Section
        item {
            Text(
                text = "Discover Recipes",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            SearchBar(searchQuery)
        }

        // Categories Section
        item {
            Text(
                text = "Categories",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            CategoriesRow(categories = listOf("All", "Breakfast", "Lunch", "Dinner", "Dessert"))
        }

        // Popular Recipes Section
        item {
            Text(
                text = "Popular Recipes",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        if (isLoading.value) {
            item {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        } else if (isError.value) {
            item {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Failed to load recipes", color = Color.Red)
                }
            }
        } else {
            items(recipes.value.size) { index ->
                RecipeCard(
                    recipeTitle = recipes.value[index].name,
                    recipeImage = recipes.value[index].imageUrl,
                    starRating = recipes.value[index].rating.toString(),
                    prepTime = recipes.value[index].cookTime,
                    onClick = {
                        val context = LocalContext.current
                        val intent = Intent(context, RecipeDetailActivity::class.java).apply {
                            putExtra("recipeName", recipes.value[index].name)
                            putExtra("recipeImage", recipes.value[index].imageUrl)
                            putExtra("recipePrepTime", recipes.value[index].cookTime)
                            putExtra("recipeRating", recipes.value[index].rating.toString())
                            putExtra("recipeSteps", recipes.value[index].cookingInstructions)
                        }
                        context.startActivity(intent)
                    }
                )
            }
        }
    }
}

@Composable
fun SearchBar(searchQuery: MutableState<TextFieldValue>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        BasicTextField(
            value = searchQuery.value,
            onValueChange = { searchQuery.value = it },
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { innerTextField ->
                if (searchQuery.value.text.isEmpty()) {
                    Text(text = "Search recipes...", color = Color.Gray)
                }
                innerTextField()
            }
        )
    }
}

@Composable
fun CategoriesRow(categories: List<String>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(categories.size) { index ->
            Chip(label = categories[index], onClick = { /* TODO: Handle category click */ })
        }
    }
}

@Composable
fun Chip(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(text = label, color = Color.White)
    }
}


@Composable
fun RecipeCard(recipeTitle: String, recipeImage: String, starRating: String, prepTime: String, onClick: @Composable () -> Unit) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .clickable {
                val intent = Intent(context, RecipeDetailActivity::class.java).apply {
                    putExtra("recipeName", recipeTitle)
                    putExtra("recipeImage", recipeImage)
                    putExtra("recipePrepTime", prepTime)
                    putExtra("recipeRating", starRating)
                    putExtra("recipeSteps", "Cooking instructions here") // Replace with actual steps
                }
                context.startActivity(intent)
            }
            .padding(16.dp)
    ) {
        AsyncImage(
            model = recipeImage,
            contentDescription = "Recipe Image",
            modifier = Modifier
                .size(78.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column (
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ){
            Row {
                Text(
                    text = recipeTitle,
                    style = MaterialTheme.typography.titleMedium,
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.prep_time_icon),
                    contentDescription = "Clock Icon",
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = prepTime,
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.weight(1f))

                Image(
                    painter = painterResource(id = R.drawable.star_rating_icon),
                    contentDescription = "Star Icon",
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = starRating,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(end = 16.dp)
                )
            }
        }
    }
}
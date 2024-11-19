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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import coil3.compose.AsyncImage
import com.example.recipe_app.model.Recipe
import com.example.recipe_app.viewmodel.RecipeViewModel
import com.example.recipe_app.viewmodel.STATE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val route: String
) {
    HOME("Home", Icons.Filled.Home, "home"),
    SETTINGS("Settings", Icons.Filled.Settings, "settings")
}

@Composable
fun BottomNavigationBar(
    currentRoute: String,
    onNavigationSelected: (String) -> Unit
) {
    NavigationBar {
        NavigationItem.values().forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = { onNavigationSelected(item.route) }
            )
        }
    }
}

class MainActivity : ComponentActivity() {
    private lateinit var recipeViewModel: RecipeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        setContent {
            RecipeappTheme(darkTheme = false) {
                var currentRoute by remember { mutableStateOf(NavigationItem.HOME.route) }

                Scaffold (
                    bottomBar = {
                        BottomNavigationBar(
                            currentRoute = currentRoute,
                            onNavigationSelected = { route ->
                                currentRoute = route
                            }
                        )
                    }
                ) { padding ->
                    when (currentRoute) {
                        NavigationItem.HOME.route -> HomeScreen(
                            recipeViewModel = recipeViewModel,
                            modifier = Modifier.padding(padding)
                        )
                        NavigationItem.SETTINGS.route -> SettingsScreen(
                            onBackPressed = { currentRoute = NavigationItem.SETTINGS.route },
                            modifier = Modifier.padding(padding)
                        )
                    }
                }
            }
        }
    }

    private fun initViewModel() {
        recipeViewModel = ViewModelProvider(this@MainActivity)[RecipeViewModel::class.java]
    }
}

@Composable
fun HomeScreen(
    recipeViewModel: RecipeViewModel,
    modifier: Modifier = Modifier
) {
    val searchQuery = remember { mutableStateOf(TextFieldValue("")) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

            Text(
                text = stringResource(id = R.string.discover_recipes),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            SearchBar(searchQuery, recipeViewModel)

            Text(
                text = stringResource(id = R.string.categories),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            if (recipeViewModel.categoriesResponse.isEmpty())
                recipeViewModel.getCategories()
            CategoriesRow(recipeViewModel.categoriesResponse.map { it.name }, recipeViewModel)


            Text(
                text = stringResource(id = R.string.popular_recipes),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 16.dp)
            )


        RecipesList(recipes = recipeViewModel.recipesResponse, recipeViewModel = recipeViewModel)
        if (recipeViewModel.recipesResponse.isEmpty())
            recipeViewModel.getRecipes()

        if (recipeViewModel.state == STATE.LOADING) {
            CircularProgressIndicator(modifier = Modifier.padding(50.dp))
        }
    }
}

@Composable
fun RecipesList(recipes: List<Recipe>, recipeViewModel: RecipeViewModel) {
    val scrollState = rememberLazyListState()
    val isItemReachEndScroll by remember {
        derivedStateOf {
            scrollState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ==
                    scrollState.layoutInfo.totalItemsCount - 1
        }
    }


    LaunchedEffect(key1 = isItemReachEndScroll, block = {
        recipeViewModel.getMoreRecipes()
    })



    LazyColumn(
        state = scrollState,
        modifier = Modifier.fillMaxSize()
    ) {
        items(recipes.size) { index ->
            println("Recipe: ${recipes[index].ingredientsParts}")

            RecipeCard(
                recipeTitle = recipes[index].name,
                recipeImage = recipes[index].imageUrl,
                starRating = recipes[index].rating.toString(),
                prepTime = recipes[index].cookTime,
                ingredientsParts = recipes[index].ingredientsParts,
                ingredientsQuantities = recipes[index].ingredientsQuantities,
                cookingInstructions = recipes[index].cookingInstructions
            )
        }
    }
}

@Composable
fun SearchBar(searchQuery: MutableState<TextFieldValue>, recipeViewModel: RecipeViewModel) {
    var searchJob by remember { mutableStateOf<Job?>(null) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        BasicTextField(
            value = searchQuery.value,
            onValueChange = { newValue ->
                searchQuery.value = newValue
                // Annuler la recherche précédente
                searchJob?.cancel()
                // Démarrer une nouvelle recherche avec un délai
                searchJob = CoroutineScope(Dispatchers.Main).launch {
                    delay(300) // Délai de 300ms pour éviter trop d'appels
                    recipeViewModel.searchRecipes(name = newValue.text, category = "")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { innerTextField ->
                if (searchQuery.value.text.isEmpty()) {
                    Text(text = stringResource(R.string.search_recipes), color = Color.Gray)
                }
                innerTextField()
            }
        )
    }
}

@Composable
fun CategoriesRow(categories: List<String>, recipeViewModel: RecipeViewModel) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(categories.size) { index ->
            Chip(label = categories[index], onClick = {
                recipeViewModel.searchRecipes(name = "", category = categories[index])
            })
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
fun RecipeCard(recipeTitle: String, recipeImage: String, starRating: String, prepTime: String, ingredientsParts: String, ingredientsQuantities: String, cookingInstructions: String) {
    val context = LocalContext.current

    println("ingredientsParts: $ingredientsParts")
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
                    putExtra("recipeIngredientsParts", ingredientsParts)
                    putExtra("recipeIngredientsQuantities", ingredientsQuantities)
                    putExtra("recipeSteps", cookingInstructions)
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
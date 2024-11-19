package com.example.recipe_app


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.recipe_app.ui.theme.RecipeappTheme
import java.util.*

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecipeappTheme {
                SettingsScreen(
                    onBackPressed = { finish() }
                )
            }
        }
    }

    companion object {
        private const val PREFS_NAME = "RecipeAppPrefs"
        private const val LANGUAGE_KEY = "language"

        fun getStoredLanguage(context: Context): String {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            return prefs.getString(LANGUAGE_KEY, "en") ?: "en"
        }

        fun storeLanguage(context: Context, languageCode: String) {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            prefs.edit().putString(LANGUAGE_KEY, languageCode).apply()
        }

        fun updateLocale(activity: Activity, languageCode: String) {
            val locale = Locale(languageCode)
            Locale.setDefault(locale)
            val config = activity.resources.configuration
            config.setLocale(locale)
            activity.createConfigurationContext(config)
            activity.resources.updateConfiguration(config, activity.resources.displayMetrics)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isEnglish by remember {
        mutableStateOf(SettingsActivity.getStoredLanguage(context) == "en")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Language Settings Section
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.language_settings),
                        style = MaterialTheme.typography.titleMedium
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.switch_to_french),
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Switch(
                            checked = !isEnglish,
                            onCheckedChange = { isChecked ->
                                isEnglish = !isChecked
                                val newLanguage = if (isChecked) "fr" else "en"
                                SettingsActivity.storeLanguage(context, newLanguage)

                                // Update locale and restart activity
                                val activity = context as? Activity
                                activity?.let {
                                    SettingsActivity.updateLocale(it, newLanguage)
                                    // Restart the app to apply changes
                                    val intent = Intent(context, MainActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    context.startActivity(intent)
                                    activity.finish()
                                }
                            }
                        )
                    }
                }
            }

            // Other Settings
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.app_settings),
                        style = MaterialTheme.typography.titleMedium
                    )

                    // Notifications Setting
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.enable_notifications),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Switch(
                            checked = true,
                            onCheckedChange = { /* Handle notifications setting */ }
                        )
                    }

                    // Dark Mode Setting
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.dark_mode),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Switch(
                            checked = false,
                            onCheckedChange = { /* Handle dark mode setting */ }
                        )
                    }
                }
            }

            // App Info Card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.app_info),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = stringResource(R.string.version, "1.0.0"),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
package com.aetherCorp.kitchencompanion

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aetherCorp.kitchencompanion.features.recipes.data.RecipeRepository
import com.aetherCorp.kitchencompanion.features.recipes.di.RecipeViewModelFactory
import com.aetherCorp.kitchencompanion.features.recipes.ui.RecipeScreen
import com.aetherCorp.kitchencompanion.features.recipes.ui.RecipeViewModel
import com.aetherCorp.kitchencompanion.ui.theme.KitchenCompanionTheme

class MainActivity : ComponentActivity() {
    private val recipeRepository = RecipeRepository()
    private val recipeFactory = RecipeViewModelFactory(recipeRepository)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            KitchenCompanionTheme {

                    RecipeScreen(recipeFactory)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScreenPreview() {

    KitchenCompanionTheme {

            RecipeScreen()

    }
}
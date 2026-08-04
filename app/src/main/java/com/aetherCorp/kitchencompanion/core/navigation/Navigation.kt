package com.aetherCorp.kitchencompanion.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aetherCorp.kitchencompanion.features.recipes.di.RecipeViewModelFactory
import com.aetherCorp.kitchencompanion.features.recipes.ui.RecipeScreen
import kotlinx.serialization.Serializable

@Serializable
object RecipeRoute

@Composable
fun AppNavHost(recipeViewModelFactory: RecipeViewModelFactory) {

    val navController = rememberNavController()


    NavHost(navController, startDestination = RecipeRoute) {
        composable<RecipeRoute> { RecipeScreen(recipeViewModelFactory) }
    }
}
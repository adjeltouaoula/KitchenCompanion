package com.aetherCorp.kitchencompanion.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aetherCorp.kitchencompanion.features.addrecipe.ui.AddRecipeScreen
import com.aetherCorp.kitchencompanion.features.recipes.ui.RecipeScreen

@Composable
fun AppNavHost() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = RecipeRoute) {
        composable<RecipeRoute> {
            RecipeScreen(
                onAddRecipeClicked = {
                    navController.navigate(AddRecipeRoute)
                })
        }
        composable<AddRecipeRoute> {
            AddRecipeScreen(
                onRecipeAdded = {
                    navController.popBackStack()
                }
            )
        }
    }
}
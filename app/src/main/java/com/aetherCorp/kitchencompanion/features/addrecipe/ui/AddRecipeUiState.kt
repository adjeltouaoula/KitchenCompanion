package com.aetherCorp.kitchencompanion.features.addrecipe.ui

import com.aetherCorp.kitchencompanion.features.recipes.domain.Ingredient

data class AddRecipeUiState(
    // for now we will set only the recipe name
    val recipeName: String = "",
    val ingredients: List<IngredientFormUiState> = listOf(),
    val canAddIngredient: Boolean = true
)
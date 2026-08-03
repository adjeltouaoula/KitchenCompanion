package com.aetherCorp.kitchencompanion.features.recipes.ui

import com.aetherCorp.kitchencompanion.features.recipes.domain.Recipe

data class RecipeUiState(
    val recipes: List<Recipe> = emptyList()
)

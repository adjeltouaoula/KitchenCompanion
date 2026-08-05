package com.aetherCorp.kitchencompanion.features.addrecipe.ui

import com.aetherCorp.kitchencompanion.features.recipes.domain.Recipe

data class AddRecipeUiState(
    // for now we will set only the recipe name
    val recipeName: String = ""
)
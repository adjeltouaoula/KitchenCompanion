package com.aetherCorp.kitchencompanion.features.addrecipe.ui

data class AddRecipeUiState(
    val recipeName: String = "",
    val ingredients: List<IngredientFormUiState> = emptyList(),
    val canAddIngredient: Boolean = true
)
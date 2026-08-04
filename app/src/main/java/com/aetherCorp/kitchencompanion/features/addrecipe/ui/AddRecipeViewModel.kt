package com.aetherCorp.kitchencompanion.features.addrecipe.ui

import androidx.lifecycle.ViewModel
import com.aetherCorp.kitchencompanion.features.recipes.data.RecipeRepository
import com.aetherCorp.kitchencompanion.features.recipes.domain.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddRecipeViewModel(
    private val recipeRepository: RecipeRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<AddRecipeUiState>(AddRecipeUiState())

    val uiState: StateFlow<AddRecipeUiState> = _uiState.asStateFlow()

    fun addRecipeClicked(recipe: Recipe) {
        recipeRepository.addRecipe(recipe)

        // TODO: UPDATE
    }
}
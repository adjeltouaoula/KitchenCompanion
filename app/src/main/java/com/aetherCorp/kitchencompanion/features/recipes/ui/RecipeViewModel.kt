package com.aetherCorp.kitchencompanion.features.recipes.ui

import androidx.lifecycle.ViewModel
import com.aetherCorp.kitchencompanion.features.recipes.data.RecipeRepository
import com.aetherCorp.kitchencompanion.features.recipes.domain.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RecipeViewModel(
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<RecipeUiState>(RecipeUiState())
    val uiState: StateFlow<RecipeUiState> = _uiState.asStateFlow()

    private fun updateUiState() {
        _uiState.value = RecipeUiState(recipeRepository.getRecipes())
    }

    init {
        updateUiState()
    }
}
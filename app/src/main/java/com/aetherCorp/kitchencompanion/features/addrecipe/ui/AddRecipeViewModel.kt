package com.aetherCorp.kitchencompanion.features.addrecipe.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aetherCorp.kitchencompanion.features.recipes.data.RecipeRepository
import com.aetherCorp.kitchencompanion.features.recipes.domain.Recipe
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddRecipeViewModel(
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AddRecipeUiState>(AddRecipeUiState())
    val uiState: StateFlow<AddRecipeUiState> = _uiState.asStateFlow()

    private val _sharedFlow = MutableSharedFlow<AddRecipeEvent>()
    val sharedFlow: SharedFlow<AddRecipeEvent> = _sharedFlow.asSharedFlow()

    fun updateUiState(recipeName: String) {
        _uiState.value = AddRecipeUiState(recipeName)
    }

    fun onRecipeNameChanges(recipeName: String) {
        updateUiState(recipeName)
    }

    fun onValidateRecipeNameClicked() {
        val recipe = Recipe(
            // TODO: fake id and will change
            id = 1,
            name = _uiState.value.recipeName,
            ingredients = listOf()
        )

        viewModelScope.launch {
            _sharedFlow.emit(
                if
                        (recipeRepository.addRecipe(recipe))
                    AddRecipeEvent.RecipeAdded
                else
                    AddRecipeEvent.AddRecipeFailed
            )
        }
    }
}
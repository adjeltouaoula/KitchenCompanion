package com.aetherCorp.kitchencompanion.features.addrecipe.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aetherCorp.kitchencompanion.features.recipes.data.RecipeRepository
import com.aetherCorp.kitchencompanion.features.recipes.domain.Ingredient
import com.aetherCorp.kitchencompanion.features.recipes.domain.QuantityUnit
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

    fun updateUiState(uiState: AddRecipeUiState) {
        _uiState.value = uiState
    }

    fun onRecipeNameChanges(recipeName: String) {
        val uiState = _uiState.value.copy(recipeName = recipeName)
        updateUiState(uiState)
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

    fun onAddIngredientClicked() {
        val ingredient = IngredientFormUiState(
            name = "",
            purchasePrice = "",
            purchaseQuantity ="",
            purchaseUnit = null
        )

        val ingredients = _uiState.value.ingredients.toMutableList()
        ingredients.add(ingredient)

        val uiState = _uiState.value.copy(ingredients = ingredients)
        updateUiState(uiState)
    }

    fun onIngredientNameChanged(name: String, ingredientIndex: Int) {
        updateIngredient(ingredientIndex) {
            it.copy(name = name)
        }
    }

    fun onIngredientPriceChanged(price: String, ingredientIndex: Int) {
        updateIngredient(ingredientIndex) {
            it.copy(purchasePrice = price)
        }
    }

    fun onIngredientQuantityChanged(quantityUnit: String, ingredientIndex: Int) {
        updateIngredient(ingredientIndex) {
            it.copy(purchaseQuantity = quantityUnit)
        }
    }

    fun onIngredientUnitChanged(unit: QuantityUnit, ingredientIndex: Int) {
        updateIngredient(ingredientIndex) {
            it.copy(purchaseUnit = unit)
        }
    }

    private fun updateIngredient(
        ingredientIndex: Int,
        update: (IngredientFormUiState) -> IngredientFormUiState
    ) {
        val ingredients = _uiState.value.ingredients.toMutableList()
        ingredients[ingredientIndex] = update(ingredients[ingredientIndex])

        updateUiState(
            _uiState.value.copy(
                ingredients = ingredients
            )
        )
    }
}
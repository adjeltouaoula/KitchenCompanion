package com.aetherCorp.kitchencompanion.features.addrecipe.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aetherCorp.kitchencompanion.features.recipes.data.RecipeRepository
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

    private val _uiState = MutableStateFlow(AddRecipeUiState())
    val uiState: StateFlow<AddRecipeUiState> = _uiState.asStateFlow()

    private val _addRecipeSharedFlow = MutableSharedFlow<AddRecipeEvent>()
    val addRecipeSharedFlow: SharedFlow<AddRecipeEvent> =
        _addRecipeSharedFlow.asSharedFlow()


    fun updateUiState(uiState: AddRecipeUiState) {
        _uiState.value = uiState.copy(
            canAddIngredient = canAddIngredient(uiState.ingredients)
        )
    }


    fun onRecipeNameChanged(recipeName: String) {
        updateUiState(
            _uiState.value.copy(
                recipeName = recipeName
            )
        )
    }


    fun onAddIngredientClicked() {
        val ingredients = _uiState.value.ingredients.toMutableList()

        ingredients.add(
            IngredientFormUiState(
                name = "",
                purchasePrice = "",
                purchaseQuantity = "",
                purchaseUnit = null
            )
        )

        updateUiState(
            _uiState.value.copy(
                ingredients = ingredients
            )
        )
    }


    fun onIngredientNameChanged(
        name: String,
        ingredientIndex: Int
    ) {
        updateIngredient(ingredientIndex) {
            it.copy(name = name)
        }
    }


    fun onIngredientPriceChanged(
        price: String,
        ingredientIndex: Int
    ) {
        updateIngredient(ingredientIndex) {
            it.copy(purchasePrice = price)
        }
    }


    fun onIngredientQuantityChanged(
        quantity: String,
        ingredientIndex: Int
    ) {
        updateIngredient(ingredientIndex) {
            it.copy(purchaseQuantity = quantity)
        }
    }


    fun onIngredientUnitChanged(
        unit: QuantityUnit,
        ingredientIndex: Int
    ) {
        updateIngredient(ingredientIndex) {
            it.copy(purchaseUnit = unit)
        }
    }


    private fun updateIngredient(
        ingredientIndex: Int,
        update: (IngredientFormUiState) -> IngredientFormUiState
    ) {
        val ingredients = _uiState.value.ingredients.toMutableList()

        ingredients[ingredientIndex] =
            update(ingredients[ingredientIndex])

        updateUiState(
            _uiState.value.copy(
                ingredients = ingredients
            )
        )
    }


    private fun canAddIngredient(
        ingredients: List<IngredientFormUiState>
    ): Boolean {

        if (ingredients.isEmpty()) {
            return true
        }

        val ingredient = ingredients.last()

        return ingredient.name.isNotEmpty()
                && ingredient.purchasePrice
            .toDoubleOrNull()
            ?.let { it > 0 } == true
                && ingredient.purchaseQuantity
            .toDoubleOrNull()
            ?.let { it > 0 } == true
                && ingredient.purchaseUnit != null
    }


    fun onValidateRecipeClicked() {

        val recipe = Recipe(
            // TODO: fake id
            id = 1,
            name = _uiState.value.recipeName,
            ingredients = listOf()
        )

        viewModelScope.launch {
            _addRecipeSharedFlow.emit(
                if (recipeRepository.addRecipe(recipe)) {
                    AddRecipeEvent.RecipeAdded
                } else {
                    AddRecipeEvent.AddRecipeFailed
                }
            )
        }
    }
}
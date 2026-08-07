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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddRecipeViewModel(
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddRecipeUiState())
    val uiState: StateFlow<AddRecipeUiState> = _uiState.asStateFlow()

    private val _addRecipeSharedFlow = MutableSharedFlow<AddRecipeEvent>()
    val addRecipeSharedFlow: SharedFlow<AddRecipeEvent> =
        _addRecipeSharedFlow.asSharedFlow()


    // -------------------------
    // Recipe
    // -------------------------

    fun onRecipeNameChanged(name: String) {
        updateUiState {
            it.copy(recipeName = name)
        }
    }

    fun onValidateRecipeClicked() {
        val recipe = Recipe(
            // TODO: fake id, will change
            id = 1,
            name = _uiState.value.recipeName,
            ingredients = emptyList()
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


    // -------------------------
    // Ingredients
    // -------------------------

    fun onAddIngredientClicked() {
        val ingredients = _uiState.value.ingredients.toMutableList()

        ingredients.add(
            IngredientFormUiState()
        )

        updateUiState {
            it.copy(
                ingredients = ingredients,
                // The user can't add any new ingredients before completing this one
                canAddIngredient = false
            )
        }
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


    // -------------------------
    // State updates
    // -------------------------

    private fun updateUiState(
        update: (AddRecipeUiState) -> AddRecipeUiState
    ) {
        _uiState.value = update(_uiState.value)
    }

    private fun updateIngredient(
        ingredientIndex: Int,
        update: (IngredientFormUiState) -> IngredientFormUiState
    ) {
        val ingredients = _uiState.value.ingredients.toMutableList()

        val updatedIngredient = update(
            ingredients[ingredientIndex]
        )

        ingredients[ingredientIndex] =
            validateIngredient(updatedIngredient)

        updateUiState {
            it.copy(
                ingredients = ingredients,
                canAddIngredient = canAddIngredient(ingredients)
            )
        }
    }


    // -------------------------
    // Validation
    // -------------------------

    private fun validateIngredient(
        ingredient: IngredientFormUiState
    ): IngredientFormUiState {

        val nameError =
            if (ingredient.name.isBlank()) {
                "Le nom est obligatoire"
            } else {
                null
            }

        val price =
            ingredient.purchasePrice.toDoubleOrNull()

        val priceError =
            when {
                ingredient.purchasePrice.isBlank() ->
                    "Le prix est obligatoire"

                price == null ->
                    "Le prix doit être un nombre"

                price <= 0 ->
                    "Le prix doit être supérieur à 0"

                else ->
                    null
            }

        val quantity =
            ingredient.purchaseQuantity.toDoubleOrNull()

        val quantityError =
            when {
                ingredient.purchaseQuantity.isBlank() ->
                    "La quantité est obligatoire"

                quantity == null ->
                    "La quantité doit être un nombre"

                quantity <= 0 ->
                    "La quantité doit être supérieure à 0"

                else ->
                    null
            }

        val unitError =
            if (ingredient.purchaseUnit == null) {
                "L'unité est obligatoire"
            } else {
                null
            }

        return ingredient.copy(
            nameError = nameError,
            purchasePriceError = priceError,
            purchaseQuantityError = quantityError,
            purchaseUnitError = unitError
        )
    }

    private fun canAddIngredient(
        ingredients: List<IngredientFormUiState>
    ): Boolean {

        if (ingredients.isEmpty()) {
            return true
        }

        val lastIngredient = ingredients.last()

        return lastIngredient.nameError == null &&
                lastIngredient.purchasePriceError == null &&
                lastIngredient.purchaseQuantityError == null &&
                lastIngredient.purchaseUnitError == null
    }
}
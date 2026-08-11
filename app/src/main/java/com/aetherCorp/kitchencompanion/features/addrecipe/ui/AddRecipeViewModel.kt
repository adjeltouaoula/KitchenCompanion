package com.aetherCorp.kitchencompanion.features.addrecipe.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aetherCorp.kitchencompanion.features.recipes.data.RecipeRepository
import com.aetherCorp.kitchencompanion.features.recipes.domain.Ingredient
import com.aetherCorp.kitchencompanion.features.recipes.domain.QuantityUnit
import com.aetherCorp.kitchencompanion.features.recipes.domain.Recipe
import com.aetherCorp.kitchencompanion.features.recipes.domain.RecipeIngredient
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddRecipeViewModel(
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddRecipeUiState())
    val uiState: SharedFlow<AddRecipeUiState> = _uiState.asStateFlow()

    private val _addRecipeSharedFlow = MutableSharedFlow<AddRecipeEvent>()
    val addRecipeSharedFlow: SharedFlow<AddRecipeEvent> =
        _addRecipeSharedFlow.asSharedFlow()


    // --------------------------------------------------
    // Recipe
    // --------------------------------------------------

    fun onRecipeNameChanged(name: String) {
        _uiState.update {
            it.copy(
                recipeName = name,
                showError = false
            )
        }
    }

    fun onValidateRecipeClicked() {

        val recipeNameValid = validateRecipeName()
        val ingredientsValid = validateAllIngredients()

        if (!recipeNameValid || !ingredientsValid) {
            return
        }

        val recipe = buildRecipe()

        viewModelScope.launch {
            val event =
                if (recipeRepository.addRecipe(recipe)) {
                    AddRecipeEvent.RecipeAdded
                } else {
                    AddRecipeEvent.AddRecipeFailed
                }

            _addRecipeSharedFlow.emit(event)
        }
    }


    // --------------------------------------------------
    // Ingredients
    // --------------------------------------------------

    fun onAddIngredientClicked() {

        if (_uiState.value.ingredients.isEmpty()) {
            addEmptyIngredient()
            return
        }

        val lastIngredient = _uiState.value.ingredients.last()
        val validatedIngredient = validateIngredient(lastIngredient)

        if (isIngredientValid(validatedIngredient)) {
            addEmptyIngredient()
        } else {
            updateLastIngredient(
                validatedIngredient.copy(showErrors = true)
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


    // --------------------------------------------------
    // State updates
    // --------------------------------------------------

    private fun addEmptyIngredient() {
        _uiState.update {
            it.copy(
                ingredients = it.ingredients + IngredientFormUiState()
            )
        }
    }

    private fun updateIngredient(
        ingredientIndex: Int,
        update: (IngredientFormUiState) -> IngredientFormUiState
    ) {
        _uiState.update { state ->

            val ingredients = state.ingredients.toMutableList()

            val updatedIngredient =
                update(ingredients[ingredientIndex])

            ingredients[ingredientIndex] =
                validateIngredient(updatedIngredient)

            state.copy(
                ingredients = ingredients
            )
        }
    }

    private fun updateLastIngredient(
        ingredient: IngredientFormUiState
    ) {
        _uiState.update { state ->

            val ingredients = state.ingredients.toMutableList()

            ingredients[ingredients.lastIndex] = ingredient

            state.copy(
                ingredients = ingredients
            )
        }
    }


    // --------------------------------------------------
    // Validation
    // --------------------------------------------------

    private fun validateRecipeName(): Boolean {

        val isValid = _uiState.value.recipeName.isNotBlank()

        _uiState.update {
            it.copy(
                showError = !isValid
            )
        }

        return isValid
    }

    private fun validateAllIngredients(): Boolean {

        var allValid = true

        _uiState.update { state ->

            val validatedIngredients =
                state.ingredients.map { ingredient ->

                    val validatedIngredient =
                        validateIngredient(ingredient)

                    if (!isIngredientValid(validatedIngredient)) {
                        allValid = false
                    }

                    validatedIngredient.copy(
                        showErrors = true
                    )
                }

            state.copy(
                ingredients = validatedIngredients
            )
        }

        return allValid
    }

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

    private fun isIngredientValid(
        ingredient: IngredientFormUiState
    ): Boolean {
        return ingredient.nameError == null &&
                ingredient.purchasePriceError == null &&
                ingredient.purchaseQuantityError == null &&
                ingredient.purchaseUnitError == null
    }


    // --------------------------------------------------
    // Recipe creation
    // --------------------------------------------------

    private fun buildRecipe(): Recipe {

        val ingredients = _uiState.value.ingredients.map { ingredient ->

            RecipeIngredient(
                ingredient = Ingredient(
                    name = ingredient.name,
                    purchasePrice = ingredient.purchasePrice.toDouble(),
                    purchaseQuantity = ingredient.purchaseQuantity.toDouble(),
                    purchaseUnit = ingredient.purchaseUnit!!
                ),
                quantity = ingredient.purchaseQuantity.toDouble(),
                unit = ingredient.purchaseUnit
            )
        }

        return Recipe(
            id = 1,
            name = _uiState.value.recipeName.trim(),
            ingredients = ingredients
        )
    }
}

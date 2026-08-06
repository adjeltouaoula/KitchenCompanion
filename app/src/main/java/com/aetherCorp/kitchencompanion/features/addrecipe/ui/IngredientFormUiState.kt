package com.aetherCorp.kitchencompanion.features.addrecipe.ui

import com.aetherCorp.kitchencompanion.features.recipes.domain.QuantityUnit

data class IngredientFormUiState(
    val name: String,
    val purchasePrice: String,
    val purchaseQuantity: String,
    val purchaseUnit: QuantityUnit?
)
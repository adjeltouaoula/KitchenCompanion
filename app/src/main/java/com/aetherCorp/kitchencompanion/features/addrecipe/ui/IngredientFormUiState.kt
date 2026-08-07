package com.aetherCorp.kitchencompanion.features.addrecipe.ui

import com.aetherCorp.kitchencompanion.features.recipes.domain.QuantityUnit

data class IngredientFormUiState(
    val name: String = "",
    val purchasePrice: String = "",
    val purchaseQuantity: String = "",
    val purchaseUnit: QuantityUnit? = null,

    val nameError: String? = null,
    val purchasePriceError: String? = null,
    val purchaseQuantityError: String? = null,
    val purchaseUnitError: String? = null
)
package com.aetherCorp.kitchencompanion.features.recipes.domain

data class Ingredient(
    val name: String,
    val purchasePrice: Double,
    val purchaseQuantity: Double,
    val purchaseUnit: QuantityUnit
)

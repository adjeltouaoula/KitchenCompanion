package com.aetherCorp.kitchencompanion.features.recipes.domain

data class RecipeIngredient(
    val ingredient: Ingredient,
    val quantity: Double,
    val unit: QuantityUnit
)

enum class QuantityUnit {
    G,
    KG,
    ML,
    CL,
    L,
    PIECE,
    TSP,    // cuillère à café
    TBSP,   // cuillère à soupe
    PINCH   // pincée

}

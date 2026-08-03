package com.aetherCorp.kitchencompanion.features.recipes.domain

data class Recipe(
    val id: Long,
    val name: String,
    val ingredients: List<RecipeIngredient>
)
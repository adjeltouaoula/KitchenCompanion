package com.aetherCorp.kitchencompanion.core.utils

import com.aetherCorp.kitchencompanion.features.recipes.domain.Ingredient
import com.aetherCorp.kitchencompanion.features.recipes.domain.QuantityUnit
import com.aetherCorp.kitchencompanion.features.recipes.domain.Recipe
import com.aetherCorp.kitchencompanion.features.recipes.domain.RecipeIngredient

val patesCarbo = Recipe(
    id = 1,
    name = "Pâtes Carbonara",
    ingredients = listOf(
        RecipeIngredient(
            ingredient = Ingredient(
                name = "Pâtes",
                purchasePrice = 1.80,
                purchaseQuantity = 500.0,
                purchaseUnit = QuantityUnit.G
            ),
            quantity = 200.0,
            unit = QuantityUnit.G
        ),
        RecipeIngredient(
            ingredient = Ingredient(
                name = "Lardons",
                purchasePrice = 2.50,
                purchaseQuantity = 200.0,
                purchaseUnit = QuantityUnit.G
            ),
            quantity = 150.0,
            unit = QuantityUnit.G
        )
    )
)
package com.aetherCorp.kitchencompanion.features.recipes.data

import com.aetherCorp.kitchencompanion.features.recipes.domain.Recipe


interface RecipeRepository {

    fun getRecipe(recipeId: Long): Recipe?
    fun getRecipes(): List<Recipe>
    fun addRecipe(recipe: Recipe): Boolean
    fun deleteRecipe(recipe: Recipe): Boolean
    fun updateRecipe(recipe: Recipe): Boolean

}
package com.aetherCorp.kitchencompanion.features.recipes.data

import com.aetherCorp.kitchencompanion.features.recipes.domain.Recipe
import javax.inject.Inject
import kotlin.collections.contains
import kotlin.collections.remove

class FakeRecipeRepository @Inject constructor(): RecipeRepository {
    private val fakeRecipes: MutableList<Recipe> = mutableListOf()

    override fun getRecipe(recipeId: Long): Recipe? {
        val copy = fakeRecipes.find { it.id == recipeId }
        return copy
    }

    override fun getRecipes(): List<Recipe> {
        val copy = fakeRecipes.toList()
        return copy
    }

    // on fera success et error
    override fun addRecipe(recipe: Recipe): Boolean {
        return if (
            fakeRecipes.contains(
                getRecipe(recipe.id)
            )
        ) false
        else
            fakeRecipes.add(recipe)
    }

    // idem
    override fun deleteRecipe(recipe: Recipe): Boolean {
        val recipeToDelete = getRecipe(recipe.id)
        return fakeRecipes.remove(recipeToDelete)
    }

    // idem
    override fun updateRecipe(recipe: Recipe): Boolean {
        val index = fakeRecipes.indexOfFirst { it.id == recipe.id }

        if (index == -1) {
            return false
        }

        fakeRecipes[index] = recipe
        return true
    }
}
package com.aetherCorp.kitchencompanion.features.addrecipe.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aetherCorp.kitchencompanion.features.addrecipe.ui.AddRecipeViewModel
import com.aetherCorp.kitchencompanion.features.recipes.data.RecipeRepository

class AddRecipeViewModelFactory(
    private val recipeRepository: RecipeRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddRecipeViewModel::class.java))
            return AddRecipeViewModel(recipeRepository) as T
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
package com.aetherCorp.kitchencompanion.features.recipes.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aetherCorp.kitchencompanion.features.recipes.data.RecipeRepository
import com.aetherCorp.kitchencompanion.features.recipes.ui.RecipeViewModel

class RecipeViewModelFactory(
    private val recipeRepository: RecipeRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
         if (modelClass.isAssignableFrom(RecipeViewModel::class.java))
            return RecipeViewModel(recipeRepository) as T
         throw (IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}"))
    }
}
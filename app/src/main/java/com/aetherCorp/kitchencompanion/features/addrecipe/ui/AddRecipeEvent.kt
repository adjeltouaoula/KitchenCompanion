package com.aetherCorp.kitchencompanion.features.addrecipe.ui

sealed interface AddRecipeEvent {

    object RecipeAdded : AddRecipeEvent
    object AddRecipeFailed : AddRecipeEvent

}
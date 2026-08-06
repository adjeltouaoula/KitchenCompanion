package com.aetherCorp.kitchencompanion.features.addrecipe.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aetherCorp.kitchencompanion.features.addrecipe.di.AddRecipeViewModelFactory
import com.aetherCorp.kitchencompanion.features.recipes.domain.QuantityUnit

@Composable
fun AddRecipeScreen(
    viewModelFactory: AddRecipeViewModelFactory,
    onRecipeAdded: () -> Unit,
) {
    val viewModel: AddRecipeViewModel = viewModel(factory = viewModelFactory)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        viewModel.sharedFlow.collect { event ->
            when (event) {
                is AddRecipeEvent.AddRecipeFailed -> {
                    snackbarHostState.showSnackbar(
                        message = "La recette n' pas pu être ajoutée !"
                    )
                }

                is AddRecipeEvent.RecipeAdded -> {
                    snackbarHostState.showSnackbar(
                        message = "Recette ajoutée !"
                    )
                    onRecipeAdded()
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { padding ->

        Column(
            modifier = Modifier.padding(padding)
        ) {

            TextField(value = uiState.recipeName, onValueChange = viewModel::onRecipeNameChanges)

            LazyColumn(
                modifier = Modifier.padding(padding)
            ) {

                itemsIndexed(uiState.ingredients) { index, ingredient ->
                    IngredientItem(
                        ingredient = ingredient,
                        onNameChanged = {
                            viewModel.onIngredientNameChanged(
                                name = it,
                                ingredientIndex = index
                            )
                        },
                        onPriceChanged = {
                            viewModel.onIngredientPriceChanged(
                                price = it,
                                ingredientIndex = index
                            )
                        },
                        onQuantityChanged = {
                            viewModel.onIngredientQuantityChanged(
                                quantityUnit = it,
                                ingredientIndex = index
                            )
                        },
                        onUnitChanged = {
                            viewModel.onIngredientUnitChanged(
                                unit = it ?: QuantityUnit.KG,
                                ingredientIndex = index
                            )
                        }
                    )
                }
            }
            Button(onClick = viewModel::onAddIngredientClicked) {
                Text("Ajouter un nouvel ingrédient")
            }

            Button(onClick = {
                focusManager.clearFocus()
                viewModel.onValidateRecipeNameClicked()
            }) {
                Text("Valider")
            }
        }
    }
}
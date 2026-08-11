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
        viewModel.addRecipeSharedFlow.collect { event ->

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

            TextField(value = uiState.recipeName, onValueChange = viewModel::onRecipeNameChanged,
                isError = uiState.showError, supportingText = {
                    if (uiState.showError) Text("Le nom de la recette ne doit pas être vide")
                })

            LazyColumn(
                modifier = Modifier.padding(padding)
            ) {

                itemsIndexed(uiState.ingredients) { index, ingredient ->

                    IngredientItem(
                        ingredient = ingredient,
                        onNameChanged = { name ->
                            viewModel.onIngredientNameChanged(
                                name = name,
                                ingredientIndex = index
                            )
                        },
                        onPriceChanged = { price ->
                            viewModel.onIngredientPriceChanged(
                                price = price,
                                ingredientIndex = index
                            )
                        },
                        onQuantityChanged = { quantity ->
                            viewModel.onIngredientQuantityChanged(
                                quantity = quantity,
                                ingredientIndex = index
                            )
                        },
                        onUnitChanged = { unit ->
                            viewModel.onIngredientUnitChanged(
                                unit = unit,
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
                viewModel.onValidateRecipeClicked()
            }) {
                Text("Valider")
            }
        }
    }
}
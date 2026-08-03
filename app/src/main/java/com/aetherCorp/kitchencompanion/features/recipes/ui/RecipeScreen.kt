package com.aetherCorp.kitchencompanion.features.recipes.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aetherCorp.kitchencompanion.core.utils.patesCarbo

@Composable
fun RecipeScreen(viewModel: RecipeViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Kitchen Companion")
        LazyColumn() {
            if (uiState.recipes.isNotEmpty()) {
                items(uiState.recipes) { recipe ->
                    Text(recipe.name)
                }
            } else {
                item {
                    Text("Aucune recette")
                }

            }
        }


        Button(onClick = {
            viewModel.addRecipeClicked(
                patesCarbo
            )
        }) {
            Text("Ajouter une recette")
        }
    }
}
package com.aetherCorp.kitchencompanion.features.addrecipe.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.aetherCorp.kitchencompanion.features.recipes.domain.QuantityUnit

@Composable
fun IngredientItem(
    ingredient: IngredientFormUiState,
    onNameChanged: (name: String) -> Unit,
    onPriceChanged: (price: String) -> Unit,
    onQuantityChanged: (quantity: String) -> Unit,
    onUnitChanged: (quantityUnit: QuantityUnit) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Card() {
        Column() {
            with(ingredient) {
                Row() {
                    Text("Ingrédient:")
                    TextField(
                        value = name,
                        onValueChange = { newName -> onNameChanged(newName) },
                        isError = showErrors && nameError != null,
                        supportingText = {
                            if (showErrors && nameError != null) Text(
                                nameError
                            )
                        })
                }
                Row() {
                    Text("Prix:")
                    TextField(
                        value = purchasePrice,
                        onValueChange = { newPrice -> onPriceChanged(newPrice) })
                }
                Row() {
                    Text("Quantité:")
                    TextField(
                        value = purchaseQuantity,
                        onValueChange = { newQuantity -> onQuantityChanged(newQuantity) })
                }
                @OptIn(ExperimentalMaterial3Api::class)
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {

                    @OptIn(ExperimentalMaterial3Api::class)
                    TextField(
                        value = ingredient.purchaseUnit?.name ?: "Choisir une unité",
                        readOnly = true,
                        modifier = Modifier.menuAnchor(),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        onValueChange = {}
                    )

                    @OptIn(ExperimentalMaterial3Api::class)
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        QuantityUnit.entries.forEach { unit ->

                            DropdownMenuItem(
                                text = { Text(unit.name) },
                                onClick = {
                                    onUnitChanged(unit)
                                    expanded = false
                                }
                            )
                        }
                    }
                }

            }

        }
    }
}
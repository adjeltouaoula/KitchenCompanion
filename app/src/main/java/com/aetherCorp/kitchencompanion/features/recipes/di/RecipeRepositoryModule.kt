package com.aetherCorp.kitchencompanion.features.recipes.di

import com.aetherCorp.kitchencompanion.features.recipes.data.FakeRecipeRepository
import com.aetherCorp.kitchencompanion.features.recipes.data.RecipeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RecipeRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRecipeRepository(
        repository: FakeRecipeRepository
    ): RecipeRepository
}

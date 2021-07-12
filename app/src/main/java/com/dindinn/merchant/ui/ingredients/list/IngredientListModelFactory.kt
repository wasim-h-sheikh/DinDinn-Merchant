package com.dindinn.merchant.ui.ingredients.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dindinn.merchant.network.repositories.IngredientRepository

@Suppress("UNCHECKED_CAST")
class IngredientListModelFactory(
    private val repository: IngredientRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return IngredientListViewModel(repository) as T
    }
}
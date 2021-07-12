package com.dindinn.merchant.ui.ingredients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dindinn.merchant.network.repositories.IngredientRepository
import com.dindinn.merchant.network.repositories.OrdersRepository

@Suppress("UNCHECKED_CAST")
class IngredientModelFactory(
    private val repository: IngredientRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return IngredientViewModel(repository) as T
    }
}
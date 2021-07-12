package com.dindinn.merchant.ui.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dindinn.merchant.network.repositories.OrdersRepository

@Suppress("UNCHECKED_CAST")
class OrderViewModelFactory(
    private val repository: OrdersRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return OrderViewModel(repository) as T
    }
}
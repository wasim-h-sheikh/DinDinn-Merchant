package com.dindinn.merchant.network.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dindinn.merchant.network.MyApi
import com.dindinn.merchant.network.SafeApiRequest
import com.dindinn.merchant.network.responses.OrderResponse
import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception


class OrdersRepository(
    private val api: MyApi
) {

    fun getOrders(): Observable<OrderResponse?>? {
        return api.getOrders()
    }

}
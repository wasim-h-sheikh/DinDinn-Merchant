package com.dindinn.merchant.network.responses

import com.dindinn.merchant.model.Order
import com.dindinn.merchant.model.Status


class OrderResponse {
    private lateinit var data: List<Order>
    private var status: Status? = null
    fun getOrders(): List<Order> {
        return data
    }

    fun setOrders(data: List<Order>) {
        this.data = data
    }

    fun getStatus(): Status? {
        return status
    }

    fun setStatus(status: Status?) {
        this.status = status
    }


}
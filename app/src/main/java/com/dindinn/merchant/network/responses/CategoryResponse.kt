package com.dindinn.merchant.network.responses

import com.dindinn.merchant.model.Category
import com.dindinn.merchant.model.Status

class CategoryResponse {
    private lateinit var category: List<Category>
    private var status: Status? = null
    fun getCategory(): List<Category> {
        return category
    }

    fun setCategory(category: List<Category>) {
        this.category = category
    }

    fun getStatus(): Status? {
        return status
    }

    fun setStatus(status: Status?) {
        this.status = status
    }
}
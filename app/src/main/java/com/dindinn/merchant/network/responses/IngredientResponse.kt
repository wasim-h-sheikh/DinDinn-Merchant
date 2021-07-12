package com.dindinn.merchant.network.responses

import com.dindinn.merchant.model.Ingredient
import com.dindinn.merchant.model.Status

class IngredientResponse {
    private lateinit var ingredient: List<Ingredient>
    private var status: Status? = null
    fun getIngredient(): List<Ingredient> {
        return ingredient
    }

    fun setIngredient(ingredient: List<Ingredient>) {
        this.ingredient = ingredient
    }

    fun getStatus(): Status? {
        return status
    }

    fun setStatus(status: Status?) {
        this.status = status
    }


}

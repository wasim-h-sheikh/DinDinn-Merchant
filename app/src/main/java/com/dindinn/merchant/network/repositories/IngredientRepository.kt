package com.dindinn.merchant.network.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dindinn.merchant.network.MyApi
import com.dindinn.merchant.network.SafeApiRequest
import com.dindinn.merchant.network.responses.CategoryResponse
import com.dindinn.merchant.network.responses.IngredientResponse
import com.dindinn.merchant.network.responses.OrderResponse
import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception


class IngredientRepository(
    private val api: MyApi
) {

    fun getCategory(): Observable<CategoryResponse?>? {
        return api.getCategory()
    }

    fun getIngredientById(id:Int): Observable<IngredientResponse?>? {
        return api.getIngredientById(id)
    }

    fun searchIngredient(queryText:String): Observable<IngredientResponse?>? {
        return api.searchIngredient(queryText)
    }
}
package com.dindinn.merchant.network

import com.dindinn.merchant.network.responses.CategoryResponse
import com.dindinn.merchant.network.responses.IngredientResponse
import com.dindinn.merchant.network.responses.OrderResponse
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.http.*

interface MyApi {


    @GET("orders")
    fun getOrders() : Observable<OrderResponse?>?

    @GET("categories")
    fun getCategory() : Observable<CategoryResponse?>?


    @GET("ingredient_by_category")
    fun getIngredientById(
        @Query("category_id")categoryId: Int
    ) :Observable<IngredientResponse?>?


    @POST("search_ingredient")
    fun searchIngredient(
        @Query("query")searchText: String
    ) :Observable<IngredientResponse?>?

    companion object{
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ) : MyApi {

            val okkHttpclient = OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okkHttpclient)
                .baseUrl("https://3204a6ba-66e6-4293-9bb7-a26e9bf5ae92.mock.pstmn.io/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyApi::class.java)
        }
    }

}


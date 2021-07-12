package com.dindinn.merchant

import androidx.multidex.MultiDexApplication
import com.dindinn.merchant.network.MyApi
import com.dindinn.merchant.network.NetworkConnectionInterceptor
import com.dindinn.merchant.network.repositories.IngredientRepository
import com.dindinn.merchant.network.repositories.OrdersRepository
import com.dindinn.merchant.ui.ingredients.list.IngredientListModelFactory
import com.dindinn.merchant.ui.ingredients.IngredientModelFactory
import com.dindinn.merchant.ui.orders.OrderViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class MerchantApplication: MultiDexApplication(), KodeinAware {

    override val kodein = Kodein.lazy {

        import(androidXModule(this@MerchantApplication))

        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { MyApi(instance()) }
        bind() from singleton { OrdersRepository(instance()) }
        bind() from singleton { OrderViewModelFactory(instance()) }
        bind() from singleton { IngredientRepository(instance()) }
        bind() from singleton { IngredientModelFactory(instance()) }
        bind() from singleton { IngredientListModelFactory(instance()) }

    }

}
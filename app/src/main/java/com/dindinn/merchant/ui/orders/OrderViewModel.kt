package com.dindinn.merchant.ui.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dindinn.merchant.model.Order
import com.dindinn.merchant.network.repositories.OrdersRepository
import com.dindinn.merchant.network.responses.OrderResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class OrderViewModel(
    private val repository: OrdersRepository
) : ViewModel() {

    private var disposable: CompositeDisposable?
    private val orders = MutableLiveData<List<Order>>()
    private val repoLoadError = MutableLiveData<Boolean>()
    private val loading = MutableLiveData<Boolean>()
    fun getOrders(): LiveData<List<Order>> {
        return orders
    }

    fun getError(): LiveData<Boolean> {
        return repoLoadError
    }

    fun getLoading(): LiveData<Boolean> {
        return loading
    }

    private fun fetchOrders() {
        loading.value = true
        disposable!!.add(
            repository.getOrders()?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<OrderResponse?>() {

                    override fun onError(e: Throwable) {
                        repoLoadError.value = true
                        loading.value = false
                    }

                    override fun onNext(value: OrderResponse?) {
                        if (value?.getStatus()?.success != true) {
                            onError(Throwable("Status Code:" + value?.getStatus()?.statusCode + " message:" + value?.getStatus()?.message))
                            return
                        }
                        repoLoadError.value = false
                        value.getOrders().also {
                            orders.value = it
                        }

                    }

                    override fun onComplete() {
                        loading.value = false
                    }
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        if (disposable != null) {
            disposable!!.clear()
            disposable = null
        }
    }

    init {
        disposable = CompositeDisposable()
        fetchOrders()
    }
}
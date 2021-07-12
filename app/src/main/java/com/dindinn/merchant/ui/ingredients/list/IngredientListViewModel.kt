package com.dindinn.merchant.ui.ingredients.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dindinn.merchant.model.Ingredient
import com.dindinn.merchant.network.repositories.IngredientRepository
import com.dindinn.merchant.network.responses.IngredientResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class IngredientListViewModel(
    private val repository: IngredientRepository
) : ViewModel() {
    private var disposable: CompositeDisposable?
    private val ingredients = MutableLiveData<List<Ingredient>>()
    private val repoLoadError = MutableLiveData<Boolean>()
    private val loading = MutableLiveData<Boolean>()
    fun getIngredients(): LiveData<List<Ingredient>> {
        return ingredients
    }


    fun getError(): LiveData<Boolean> {
        return repoLoadError
    }

    fun getLoading(): LiveData<Boolean> {
        return loading
    }


    fun fetchIngredientById(id: Int) {
        loading.value = true
        disposable!!.add(
            repository.getIngredientById(id)?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<IngredientResponse?>() {

                    override fun onError(e: Throwable) {
                        repoLoadError.value = true
                        loading.value = false
                    }

                    override fun onNext(value: IngredientResponse?) {
                        if (value?.getStatus()?.success != true) {
                            onError(Throwable("Status Code:" + value?.getStatus()?.statusCode + " message:" + value?.getStatus()?.message))
                            return
                        }
                        repoLoadError.value = false
                        value.getIngredient().also {
                            ingredients.value = it
                        }

                    }

                    override fun onComplete() {
                        loading.value = false
                    }
                })
        )
    }

    fun searchIngredient(queryText: String) {
        loading.value = true
        disposable!!.add(
            repository.searchIngredient(queryText)?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<IngredientResponse?>() {

                    override fun onError(e: Throwable) {
                        repoLoadError.value = true
                        loading.value = false
                    }

                    override fun onNext(value: IngredientResponse?) {
                        if (value?.getStatus()?.success != true) {
                            onError(Throwable("Status Code:" + value?.getStatus()?.statusCode + " message:" + value?.getStatus()?.message))
                            return
                        }
                        repoLoadError.value = false
                        value.getIngredient().also {
                            ingredients.value = it
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
    }
}
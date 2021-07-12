package com.dindinn.merchant.ui.ingredients

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dindinn.merchant.model.Category
import com.dindinn.merchant.network.repositories.IngredientRepository
import com.dindinn.merchant.network.responses.CategoryResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class IngredientViewModel(
    private val repository: IngredientRepository
) : ViewModel() {
    private var disposable: CompositeDisposable?
    private val categories = MutableLiveData<List<Category>>()
    private val repoLoadError = MutableLiveData<Boolean>()
    private val loading = MutableLiveData<Boolean>()


    fun getCategories(): LiveData<List<Category>> {
        return categories
    }

    fun getError(): LiveData<Boolean> {
        return repoLoadError
    }

    fun getLoading(): LiveData<Boolean> {
        return loading
    }

    private fun fetchCategory() {
        loading.value = true
        disposable!!.add(
            repository.getCategory()?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<CategoryResponse?>() {

                    override fun onError(e: Throwable) {
                        repoLoadError.value = true
                        loading.value = false
                    }

                    override fun onNext(value: CategoryResponse?) {
                        if (value?.getStatus()?.success != true) {
                            onError(Throwable("Status Code:" + value?.getStatus()?.statusCode + " message:" + value?.getStatus()?.message))
                            return
                        }
                        repoLoadError.value = false
                        value.getCategory().also {
                            categories.value = it
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
        fetchCategory()
    }
}
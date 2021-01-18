package com.e.itunesapiexample.feature.productlist

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.e.itunesapiexample.base.ProductApiService
import com.e.itunesapiexample.feature.productdetail.ProductModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class ProductListViewModel: ViewModel() {
    private var disposable = CompositeDisposable()
    private var apiService = ProductApiService()

    var progressBarVisibilityObservable = ObservableBoolean(false)
    val submitListToAdapterLiveData = MutableLiveData<List<ProductModel>>()

    private var productList: ArrayList<ProductModel>? = null

    fun getProducts() {
        progressBarVisibilityObservable.set(true)
        disposable.add(
            apiService.getProducts()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProductListResponse>() {
                    override fun onSuccess(listResponse: ProductListResponse) {
                        listResponse.results?.let {
                            productList =  ArrayList(it)
                            submitListToAdapterLiveData.value = listResponse.results
                        }
                        sortProductsByPrice(false)
                        progressBarVisibilityObservable.set(false)
                    }

                    override fun onError(e: Throwable) {
                        progressBarVisibilityObservable.set(false)
                    }
                })
        )
    }

    fun sortProductsByPrice(ascendingOrder: Boolean) {
        var sortedList = productList?.sortedByDescending { it.price?.toInt() }
        if (ascendingOrder) sortedList = sortedList?.asReversed()
        submitListToAdapterLiveData.value = sortedList
    }
}
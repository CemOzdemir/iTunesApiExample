package com.e.itunesapiexample.feature.productlist

import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.e.itunesapiexample.base.ProductApiService
import com.e.itunesapiexample.feature.productdetail.ProductModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlin.properties.Delegates

class ProductListViewModel: ViewModel() {

    private companion object {
        private const val MIN_CHAR_TO_SEARCH = 3
    }
    private var disposable = CompositeDisposable()
    private var apiService = ProductApiService()

    var searchTermObservable: String? by Delegates.observable("") { _, _, _ ->
        getResults()
    }
    var progressBarVisibilityObservable = ObservableBoolean(false)
    var productListVisibilityObservable = ObservableBoolean(false)

    val submitListToAdapterLiveData = MutableLiveData<List<ProductModel>>()

    private var productList: ArrayList<ProductModel>? = null
    var selectedCategory = ProductModel.WrapperType.MOVIE

    fun getResults() {
        searchTermObservable?.takeIf { it.length >= MIN_CHAR_TO_SEARCH }?.run {
            progressBarVisibilityObservable.set(true)
            disposable.add(
                    apiService.getProducts(this, getQueryTextForCategory())
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(object : DisposableSingleObserver<ProductListResponse>() {
                                override fun onSuccess(listResponse: ProductListResponse) {
                                    listResponse.results?.let {
                                        productList = ArrayList(it)
                                        submitListToAdapterLiveData.value = listResponse.results
                                        productListVisibilityObservable.set(true)
                                    }?: productListVisibilityObservable.set(false)
                                    progressBarVisibilityObservable.set(false)
                                }

                                override fun onError(e: Throwable) {
                                    progressBarVisibilityObservable.set(false)
                                }
                            })
            )
        } ?: productListVisibilityObservable.set(false)
    }

    fun changeCategory(category: ProductModel.WrapperType) {
        submitListToAdapterLiveData.value = productList?.filter { it.wrapperType == category }
    }

    private fun getQueryTextForCategory() = when(selectedCategory) {
        ProductModel.WrapperType.MOVIE -> "movie"
        ProductModel.WrapperType.MUSIC -> "music"
        ProductModel.WrapperType.SOFTWARE -> "software"
        ProductModel.WrapperType.EBOOK -> "ebook"
    }
}
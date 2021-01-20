package com.e.itunesapiexample.feature.productlist

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

class ProductListViewModel : ViewModel() {

    private companion object {
        private const val MIN_CHAR_TO_SEARCH = 3
        private const val CATEGORY_SEARCH_KEY_MOVIE = "movie"
        private const val CATEGORY_SEARCH_KEY_MUSIC = "music"
        private const val CATEGORY_SEARCH_KEY_SOFTWARE = "software"
        private const val CATEGORY_SEARCH_KEY_EBOOK = "ebook"
    }

    private var disposable = CompositeDisposable()
    private var apiService = ProductApiService()

    var searchTermObservable: String? by Delegates.observable("") { _, _, newText ->
        handleNoResultTextLiveData.value = newText
        getResults()
    }
    var progressBarVisibilityObservable = ObservableBoolean(false)
    var productListVisibilityObservable = ObservableBoolean(false)
    var noResultTextObservable = ObservableField<String>()

    val submitListToAdapterLiveData = MutableLiveData<List<ProductModel>>()
    val handleNoResultTextLiveData = MutableLiveData<String>()

    var productList = arrayListOf<ProductModel>()
    var selectedCategory = ProductModel.WrapperType.MOVIE

    fun getResults(searchOffset: Int = 0) {
        searchTermObservable?.takeIf { it.length >= MIN_CHAR_TO_SEARCH }?.run {
            progressBarVisibilityObservable.set(true)
            disposable.add(
                apiService.getProducts(this, getQueryTextForCategory(), searchOffset.toString())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<ProductListResponse>() {
                        override fun onSuccess(listResponse: ProductListResponse) {
                            listResponse.results?.let {
                                if (searchOffset > 0) productList.addAll(it) else productList = ArrayList(it)
                                submitListToAdapterLiveData.value = productList
                            }
                            productListVisibilityObservable.set(productList.isNotEmpty())
                            progressBarVisibilityObservable.set(false)
                        }

                        override fun onError(e: Throwable) {
                            progressBarVisibilityObservable.set(false)
                        }
                    })
            )
        } ?: productListVisibilityObservable.set(false)
    }

    private fun getQueryTextForCategory() = when (selectedCategory) {
        ProductModel.WrapperType.MOVIE    -> CATEGORY_SEARCH_KEY_MOVIE
        ProductModel.WrapperType.MUSIC    -> CATEGORY_SEARCH_KEY_MUSIC
        ProductModel.WrapperType.SOFTWARE -> CATEGORY_SEARCH_KEY_SOFTWARE
        ProductModel.WrapperType.EBOOK    -> CATEGORY_SEARCH_KEY_EBOOK
    }

    fun loadMoreResult() {
        productList.let {
            if (it.size > 0 && it.size % 20 == 0) {
                getResults(it.size)
            }
        }
    }
}
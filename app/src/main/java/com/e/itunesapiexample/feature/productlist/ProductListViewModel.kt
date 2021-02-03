package com.e.itunesapiexample.feature.productlist

import android.os.Handler
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.e.itunesapiexample.base.ProductApiService
import com.e.itunesapiexample.feature.productdetail.ProductModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import kotlin.properties.Delegates

class ProductListViewModel : ViewModel() {

    private companion object {
        private const val MIN_CHAR_TO_SEARCH = 3
        private const val SEARCH_DELAY = 500L

        private const val CATEGORY_SEARCH_KEY_MOVIE = "movie"
        private const val CATEGORY_SEARCH_KEY_MUSIC = "music"
        private const val CATEGORY_SEARCH_KEY_SOFTWARE = "software"
        private const val CATEGORY_SEARCH_KEY_EBOOK = "ebook"
    }

    @Inject
    lateinit var apiService: ProductApiService

    var disposable = CompositeDisposable()

    var handler = Handler()

    var searchTermObservable: String? by Delegates.observable("") { _, oldText, newText ->
        if (newText?.trim() != oldText?.trim()) {
            handler.removeCallbacksAndMessages(null)
            handler.postDelayed({
                handleNoResultText.value = newText
                getSearchResults()
            }, SEARCH_DELAY)
        }
    }
    var progressBarVisibilityObservable = ObservableBoolean(false)
    var productListVisibilityObservable = ObservableBoolean(false)
    var noResultTextObservable = ObservableField<String>()

    private val submitListToAdapter = MutableLiveData<List<ProductModel>>()
    val submitListToAdapterLiveData: LiveData<List<ProductModel>> get() = submitListToAdapter
    private val handleNoResultText = MutableLiveData<String>()
    val handleNoResultTextLiveData: MutableLiveData<String> get() = handleNoResultText

    var productList = arrayListOf<ProductModel>()
    var selectedCategory = ProductModel.WrapperType.MOVIE

    init {
        DaggerProductListViewModelComponent.create().inject(this)
    }

    /**
     * @param searchOffset it indicates the starting point of the search.
     * e.g. if we got 20 results at hand, search will bring results starting from 20. result.
     */
    fun getSearchResults(searchOffset: Int = 0) {
        searchTermObservable?.takeIf { it.trim().length >= MIN_CHAR_TO_SEARCH }?.run {
            sendSearchRequest(this.trim(), searchOffset)
        } ?: productListVisibilityObservable.set(false)
    }

    fun loadMoreResult() {
        productList.let {
            if (it.size > 0 && it.size % 20 == 0) {
                getSearchResults(it.size)
            }
        }
    }

    private fun sendSearchRequest(searchTerm: String, searchOffset: Int) {
        progressBarVisibilityObservable.set(true)
        disposable.clear()
        disposable.add(
                apiService.getProducts(searchTerm, getQueryTextForCategory(), searchOffset.toString())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(ProductListObserver(this, searchOffset))
        )
    }

    fun handleSearchResults(listResponse: ProductListResponse?, searchOffset: Int) {
        listResponse?.results?.let {
            if (searchOffset > 0) productList.addAll(it) else productList = ArrayList(it)
            submitListToAdapter.value = productList
        }
        productListVisibilityObservable.set(productList.isNotEmpty())
        progressBarVisibilityObservable.set(false)
    }

    private fun getQueryTextForCategory() = when (selectedCategory) {
        ProductModel.WrapperType.MOVIE    -> CATEGORY_SEARCH_KEY_MOVIE
        ProductModel.WrapperType.MUSIC    -> CATEGORY_SEARCH_KEY_MUSIC
        ProductModel.WrapperType.SOFTWARE -> CATEGORY_SEARCH_KEY_SOFTWARE
        ProductModel.WrapperType.EBOOK    -> CATEGORY_SEARCH_KEY_EBOOK
    }
}
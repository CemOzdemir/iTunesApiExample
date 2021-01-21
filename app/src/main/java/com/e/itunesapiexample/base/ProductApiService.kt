package com.e.itunesapiexample.base

import com.e.itunesapiexample.feature.productlist.ProductListResponse
import io.reactivex.Single
import javax.inject.Inject

class ProductApiService {
    private companion object {
        private const val MAX_ITEM_PER_REQUEST = "20"
    }

    @Inject
    lateinit var productApi: ProductApi

    init {
        DaggerApiComponent.create().inject(this)
    }

    fun getProducts(
            searchTerm: String,
            category: String,
            offset: String = "0",
            maxItemCount: String = MAX_ITEM_PER_REQUEST
    ): Single<ProductListResponse> = productApi.getProducts(searchTerm, category, offset, maxItemCount)
}
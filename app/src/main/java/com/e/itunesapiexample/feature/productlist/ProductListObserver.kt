package com.e.itunesapiexample.feature.productlist

import io.reactivex.observers.DisposableSingleObserver

class ProductListObserver(
        private val viewModel: ProductListViewModel,
        private val searchOffset: Int = 0
) : DisposableSingleObserver<ProductListResponse?>() {
    override fun onSuccess(response: ProductListResponse) {
        viewModel.handleSearchResults(response, searchOffset)
    }

    override fun onError(e: Throwable) {

    }
}
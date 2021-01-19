package com.e.itunesapiexample.feature.productdetail

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

class ProductDetailViewModel : ViewModel() {

    val productImageUrlObservable = ObservableField<String>()
    val productNameObservable = ObservableField<String>()
    val productDescriptionObservable = ObservableField<String>()

    fun handleProductDetail(response: ProductModel?) {
        response?.run {
            productImageUrlObservable.set(artworkUrl100)
            productNameObservable.set(collectionName)
            productDescriptionObservable.set(collectionPrice)
        }
    }
}
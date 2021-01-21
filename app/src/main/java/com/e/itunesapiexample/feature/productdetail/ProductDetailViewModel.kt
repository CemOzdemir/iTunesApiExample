package com.e.itunesapiexample.feature.productdetail

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

class ProductDetailViewModel : ViewModel() {

    val productImageUrlObservable = ObservableField<String>()
    val productNameObservable = ObservableField<String>()
    val productDescriptionObservable = ObservableField<String>()
    val productPriceObservable = ObservableField<String>()

    fun handleProductDetail(product: ProductModel?) {
        product?.run {
            productImageUrlObservable.set(artworkUrl100)
            productNameObservable.set(product.collectionName.takeIf { it?.isNotBlank() == true } ?: product.trackName)
            productDescriptionObservable.set(getAvailableDescription(this))
            if (collectionPrice?.isNotBlank() == true) productPriceObservable.set("${product.collectionPrice} ${product.currency}")
        }
    }

    private fun getAvailableDescription(product: ProductModel) = when {
        product.description?.isNotBlank() == true -> product.description
        product.shortDescription?.isNotBlank() == true -> product.shortDescription
        product.primaryGenreName?.isNotBlank() == true -> product.primaryGenreName
        else -> ""
    }
}
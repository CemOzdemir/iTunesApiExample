package com.e.itunesapiexample.feature.productdetail

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.e.itunesapiexample.base.ProductApiService
import io.reactivex.disposables.CompositeDisposable

class ProductDetailViewModel : ViewModel() {
    private var disposable = CompositeDisposable()
    private var apiService = ProductApiService()

    val productImageUrlObservable = ObservableField<String>()
    val productNameObservable = ObservableField<String>()
    val productDescriptionObservable = ObservableField<String>()
    val progressBarVisibilityObservable = ObservableBoolean(false)
//
//    fun getProductDetail(productId: String?) {
//        progressBarVisibilityObservable.set(true)
//        disposable.add(
//            apiService.getProductDetail(productId)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(object : DisposableSingleObserver<ProductModel>() {
//                    override fun onSuccess(response: ProductModel) {
//                        progressBarVisibilityObservable.set(false)
//                        handleProductDetail(response)
//                    }
//
//                    override fun onError(e: Throwable) {
//                        progressBarVisibilityObservable.set(false)
//                    }
//                })
//        )
//    }
//
//    fun handleProductDetail(response: ProductModel?) {
//        response?.run {
//            productImageUrlObservable.set(imageUrl)
//            productNameObservable.set(name)
//            productDescriptionObservable.set(description)
//        }
//    }
}
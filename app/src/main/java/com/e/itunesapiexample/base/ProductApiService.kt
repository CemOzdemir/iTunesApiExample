package com.e.itunesapiexample.base

import com.e.itunesapiexample.feature.productlist.ProductListResponse
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ProductApiService {

    private companion object {
        private const val BASE_URL = "https://itunes.apple.com/"
    }

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(ProductApi::class.java)

    fun getProducts(): Single<ProductListResponse> = api.getProducts("jack johnson")
//
//    fun getProductDetail(productId: String?): Single<ProductModel> = api.getProductDetail(productId)
}
package com.e.itunesapiexample.base

import com.e.itunesapiexample.feature.productdetail.ProductModel
import com.e.itunesapiexample.feature.productlist.ProductListResponse
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class ProductApiService {
    private companion object {
        private const val BASE_URL = "https://itunes.apple.com/"
        private const val MAX_ITEM_PER_REQUEST = "20"
    }

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(ProductApi::class.java)

    fun getProducts(
            searchTerm: String,
            category: String,
            maxItemCount: String = MAX_ITEM_PER_REQUEST
    ): Single<ProductListResponse> = api.getProducts(searchTerm, category, maxItemCount)
}
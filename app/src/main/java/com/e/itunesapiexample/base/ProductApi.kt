package com.e.itunesapiexample.base

import com.e.itunesapiexample.feature.productdetail.ProductModel
import com.e.itunesapiexample.feature.productlist.ProductListResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApi {
    @GET("search")
    fun getProducts(@Query("term") searchTerm: String?,
                    @Query("media") category: String?,
                    @Query("offset") offset: String?,
                    @Query("limit") maxItem: String?): Single<ProductListResponse>
}
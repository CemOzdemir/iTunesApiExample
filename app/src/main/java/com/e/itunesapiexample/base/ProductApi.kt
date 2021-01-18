package com.e.itunesapiexample.base

import com.e.itunesapiexample.feature.productlist.ProductListResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApi {

    @GET("search")
    fun getProducts(@Query("term") searchTerm: String?): Single<ProductListResponse>

//    @GET("search?term=jack+johnson")
//    @GET("developer-application-test/cart/{product_id}/detail")
//    fun getProductDetail(@Path("product_id") productId: String?): Single<ProductModel>
}
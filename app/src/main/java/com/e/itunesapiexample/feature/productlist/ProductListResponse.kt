package com.e.itunesapiexample.feature.productlist

import com.e.itunesapiexample.feature.productdetail.ProductModel

data class ProductListResponse(
        val resultCount: Int?,
        val results: List<ProductModel>?
)
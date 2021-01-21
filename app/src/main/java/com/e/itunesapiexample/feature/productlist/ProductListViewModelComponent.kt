package com.e.itunesapiexample.feature.productlist

import com.e.itunesapiexample.di.ApiModule
import dagger.Component

@Component(modules = [ApiModule::class])
interface ProductListViewModelComponent {

    fun inject(viewModel: ProductListViewModel)
}
package com.e.itunesapiexample.base

import com.e.itunesapiexample.di.ApiModule
import dagger.Component

@Component(modules = [ApiModule::class])
interface ApiComponent {

    fun inject(service: ProductApiService)
}
package com.e.itunesapiexample.di

import com.e.itunesapiexample.base.ProductApi
import com.e.itunesapiexample.base.ProductApiService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class ApiModule {
    private companion object {
        private const val BASE_URL = "https://itunes.apple.com/"
    }

    @Provides
    fun provideProductApi(): ProductApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(ProductApi::class.java)

    @Provides
    fun provideProductApiService() = ProductApiService()
}
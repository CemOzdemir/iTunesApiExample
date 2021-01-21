package com.e.itunesapiexample.productdetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.e.itunesapiexample.feature.productdetail.ProductModel
import com.e.itunesapiexample.feature.productlist.ProductListResponse
import com.e.itunesapiexample.feature.productlist.ProductListViewModel
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.Executor

class ProductListViewModelTest {
    @get:Rule
    var rule = InstantTaskExecutorRule()

    var viewModel = ProductListViewModel()


    @Before
    fun setupRxSchedulers() {
        val immediate = object : Scheduler() {
            override fun createWorker() =  ExecutorScheduler.ExecutorWorker(Executor { it.run() }, true)
        }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { scheduler -> immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> immediate }
    }

    @Test
    fun testGetSearchResults() {
        // Given
        viewModel.searchTermObservable = ""

        // When
        viewModel.getSearchResults()

        // Then
        assertFalse(viewModel.productListVisibilityObservable.get())

        // Given
        viewModel.searchTermObservable = "baby shark"

        // When
        viewModel.getSearchResults()

        // Then
        assertTrue(viewModel.productListVisibilityObservable.get())
    }

    @Test
    fun testHandleSearchResults() {
        // No result case
        // Given
        val mockProduct = mock<ProductModel>()
        val mockProductList = listOf(mockProduct, mockProduct, mockProduct, mockProduct, mockProduct)
        var productListResponse = ProductListResponse(
                resultCount = 0,
                results = emptyList()
        )
        viewModel.productList.clear()

        // When
        viewModel.handleSearchResults(productListResponse, 0)

        // Then
        assertEquals(0, viewModel.productList.size)
        assertFalse(viewModel.productListVisibilityObservable.get())

        // Multiple results case
        // Given
        productListResponse = ProductListResponse(
                resultCount = 5,
                results = mockProductList
        )

        // When
        viewModel.handleSearchResults(productListResponse, 0)

        // Then
        assertEquals(5, viewModel.productList.size)
        assertTrue(viewModel.productListVisibilityObservable.get())

        // Load more case
        // Given
        viewModel.run {
            productList.clear()
            productList = ArrayList(mockProductList)
        }
        productListResponse = ProductListResponse(
                resultCount = 5,
                results = mockProductList
        )

        // When
        viewModel.handleSearchResults(productListResponse, 5)

        // Then
        assertEquals(10, viewModel.productList.size)
    }
}
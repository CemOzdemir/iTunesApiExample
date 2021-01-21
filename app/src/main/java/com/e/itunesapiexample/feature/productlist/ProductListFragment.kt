package com.e.itunesapiexample.feature.productlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.e.itunesapiexample.R
import com.e.itunesapiexample.databinding.FragmentProductListBinding
import com.e.itunesapiexample.feature.productdetail.ProductModel
import com.google.android.material.button.MaterialButtonToggleGroup

class ProductListFragment : Fragment() {

    companion object {
        private const val DIRECTION_BOTTOM = 1
    }

    private lateinit var viewModel: ProductListViewModel
    private lateinit var binding: FragmentProductListBinding

    private val productListAdapter = ProductListAdapter(arrayListOf())

    private val productListLiveDataObserver = Observer<List<ProductModel>> { list ->
        list?.let {
            productListAdapter.submitList(ArrayList(list))
        }
    }

    private val handleNoResultTextLiveDataObserver = Observer<String> {
        viewModel.run {
            when (it.length) {
                in 0..2 -> {
                    productList.clear()
                    noResultTextObservable.set(context?.getString(R.string.enter_text))
                }
                else -> noResultTextObservable.set(context?.resources?.getString(R.string.no_result, it))
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_list, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ProductListViewModel::class.java).apply {
            binding.viewModel = this
            submitListToAdapterLiveData.observe(viewLifecycleOwner, productListLiveDataObserver)
            handleNoResultTextLiveData.observe(viewLifecycleOwner, handleNoResultTextLiveDataObserver)
        }
        viewModel.noResultTextObservable.set(context?.getString(R.string.enter_text))
        binding.run {
            productList.run {
                layoutManager = GridLayoutManager(context, 2)
                adapter = productListAdapter
            }
            buttonGroup.addOnButtonCheckedListener(categoryChangeListener)
            productList.addOnScrollListener(onScrollChangeListener)
        }
    }

    private val categoryChangeListener = MaterialButtonToggleGroup.OnButtonCheckedListener { _, checkedId, isChecked ->
        if (isChecked) {
            when (checkedId) {
                binding.movieButton.id -> viewModel.selectedCategory = ProductModel.WrapperType.MOVIE
                binding.musicButton.id -> viewModel.selectedCategory = ProductModel.WrapperType.MUSIC
                binding.appButton.id -> viewModel.selectedCategory = ProductModel.WrapperType.SOFTWARE
                binding.bookButton.id -> viewModel.selectedCategory = ProductModel.WrapperType.EBOOK
            }
            viewModel.run {
                productList.clear()
                viewModel.getSearchResults()
            }
        }
    }

    private val onScrollChangeListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (!recyclerView.canScrollVertically(DIRECTION_BOTTOM) && newState==RecyclerView.SCROLL_STATE_IDLE) {
                viewModel.loadMoreResult()
            }
        }
    }
}
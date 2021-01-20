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
import com.e.itunesapiexample.R
import com.e.itunesapiexample.databinding.FragmentProductListBinding
import com.e.itunesapiexample.feature.productdetail.ProductModel
import com.google.android.material.button.MaterialButtonToggleGroup

class ProductListFragment : Fragment() {

    private companion object {
        private val MOVIES_BUTTON_TAG = ProductModel.WrapperType.MOVIE
        private val MUSIC_BUTTON_TAG = ProductModel.WrapperType.MUSIC
        private val APPS_BUTTON_TAG = ProductModel.WrapperType.SOFTWARE
        private val BOOKS_BUTTON_TAG = ProductModel.WrapperType.EBOOK
    }

    private lateinit var viewModel: ProductListViewModel
    private lateinit var binding: FragmentProductListBinding

    private val productListAdapter = ProductListAdapter(arrayListOf())

    private val productListLiveDataObserver = Observer<List<ProductModel>> { list ->
        list?.let {
            productListAdapter.submitList(ArrayList(list))
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
        }
        binding.productList.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = productListAdapter
        }
        binding.run {
            productList.run {
                layoutManager = GridLayoutManager(context, 2)
                adapter = productListAdapter
            }
            movieButton.run {
                tag = MOVIES_BUTTON_TAG
                setOnClickListener(groupButtonClickListener)
            }
            musicButton.run {
                tag = MUSIC_BUTTON_TAG
                setOnClickListener(groupButtonClickListener)
            }
            appButton.run {
                tag = APPS_BUTTON_TAG
                setOnClickListener(groupButtonClickListener)
            }
            bookButton.run {
                tag = BOOKS_BUTTON_TAG
                setOnClickListener(groupButtonClickListener)
            }
            buttonGroup.addOnButtonCheckedListener(categoryChangeListener)
        }
    }

    private val groupButtonClickListener = View.OnClickListener { button ->
        button.tag?.takeIf { it is ProductModel.WrapperType }?.run {
            viewModel.changeCategory(this as ProductModel.WrapperType)
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
            viewModel.getResults()
        }
    }
}
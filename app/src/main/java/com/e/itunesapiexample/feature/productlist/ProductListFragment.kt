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

class ProductListFragment : Fragment() {

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
            getProducts()
        }

        binding.productList.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = productListAdapter
        }
    }
}
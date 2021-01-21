package com.e.itunesapiexample.feature.productdetail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.e.itunesapiexample.R
import com.e.itunesapiexample.databinding.FragmentProductDetailBinding

class ProductDetailFragment : Fragment() {

    private lateinit var viewModel: ProductDetailViewModel
    private lateinit var binding: FragmentProductDetailBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_detail, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProductDetailViewModel::class.java)
        binding.viewModel = viewModel
        arguments?.let { viewModel.handleProductDetail(ProductDetailFragmentArgs.fromBundle(it).product) }
    }
}
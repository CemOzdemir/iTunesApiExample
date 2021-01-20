package com.e.itunesapiexample.feature.productlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.e.itunesapiexample.R
import com.e.itunesapiexample.feature.productdetail.ProductModel
import com.e.itunesapiexample.loadImage
import kotlinx.android.synthetic.main.item_product.view.*

class ProductListAdapter(private var productList: ArrayList<ProductModel>):
    RecyclerView.Adapter<ProductListAdapter.ProductViewHolder>() {

    class ProductViewHolder(var view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.view.run {
            product_name.text = product.collectionName
            product_price.text = product.price
            product_image.loadImage(product.artworkUrl100)
            container.setOnClickListener {
                val action = ProductListFragmentDirections.actionToProductDetailFragment(product)
                Navigation.findNavController(it).navigate(action)
            }
        }
    }

    fun submitList(list: ArrayList<ProductModel>) {
        this.productList = list
        notifyDataSetChanged()
    }

    override fun getItemCount() = productList.size
}
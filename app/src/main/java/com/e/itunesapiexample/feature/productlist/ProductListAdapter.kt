package com.e.itunesapiexample.feature.productlist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.e.itunesapiexample.R
import com.e.itunesapiexample.feature.productdetail.ProductModel
import com.e.itunesapiexample.loadImage
import kotlinx.android.synthetic.main.item_product.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ProductListAdapter(private var productList: ArrayList<ProductModel>):
    RecyclerView.Adapter<ProductListAdapter.ProductViewHolder>() {

    class ProductViewHolder(var view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.view.run {
            product_name.text = product.collectionName.takeIf { it?.isNotBlank() == true } ?: product.trackName
            product_price.text = if (product.collectionPrice?.isNotBlank() == true) "${product.collectionPrice} ${product.currency}" else ""
            product_release_date.text = getFormattedDate(product.releaseDate)
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

    private fun getFormattedDate(date: String?) = LocalDateTime.parse(
        date, DateTimeFormatter.ISO_DATE_TIME
    ).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
}
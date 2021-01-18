package com.e.itunesapiexample

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

fun ImageView.loadImage(uri: String?) {
    Glide.with(context)
            .load(uri)
            .into(this)
}

@BindingAdapter("android:imageUrl")
fun loadImageView(view: ImageView, url: String?) {
    view.loadImage(url)
}

@BindingAdapter("android:visibility")
fun changeVisibility(view: View, visibility: Boolean) {
    view.visibility = if (visibility) View.VISIBLE else View.GONE
}
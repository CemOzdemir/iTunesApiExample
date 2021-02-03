package com.e.itunesapiexample

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

const val EMPTY = ""

fun ImageView.loadImage(uri: String?) {
    Glide.with(context)
            .load(uri)
            .into(this)
}

fun TextView.setTextOrHide(textValue: String?) {
    this.run {
        if (textValue?.isNotBlank() == true) {
            text = textValue
        } else {
            visibility = View.GONE
        }
    }
}

@BindingAdapter("android:imageUrl")
fun loadImageView(view: ImageView, url: String?) {
    view.loadImage(url)
}

@BindingAdapter("android:visibility")
fun changeVisibility(view: View, visibility: Boolean) {
    view.visibility = if (visibility) View.VISIBLE else View.GONE
}
package com.nahid.meetmax.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter

private const val TAG = "BindingAdapter"

@BindingAdapter("setTimeAgo")
fun TextView.setTimeAgo(timestamp: Long) {
    text = Tools.getTimeAgo(timestamp)
}

@BindingAdapter("setImage")
fun ImageView.setImage(image: String?) {
    image?.let {
        this.setImageBitmap(ImageUtils.base64StringToBitmap(it))
    }
}

@BindingAdapter("setImageResource")
fun ImageView.setImageResource(image: Int) {
    this.setImageResource(image)
}


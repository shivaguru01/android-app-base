package com.app.core.common

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.app.core.R
import com.bumptech.glide.Glide

class BindUtils {

    companion object {
        @JvmStatic
        @BindingAdapter("imageUrl")
        fun setImageView(imageView: ImageView, imageUrl: String?) {
            if (!imageUrl.isNullOrEmpty()) {
                if (imageView.getTag(R.id.image_url) == null || imageView.getTag(R.id.image_url) != imageUrl) {
                    imageView.setImageBitmap(null)
                    imageView.setTag(R.id.image_url, imageUrl)
                    Glide.with(imageView).load(imageUrl)
                        .placeholder(R.color.green).into(imageView)
                }
            } else {
                imageView.setTag(R.id.image_url, null)
                imageView.setImageBitmap(null)
            }
        }

        @JvmStatic
        @BindingAdapter("android:src")
        fun setImageViewResource(imageView: ImageView, resourceId: Int) {
            if (resourceId > 0) {
                imageView.setImageResource(resourceId)
                imageView.visibility = View.VISIBLE
            } else {
                imageView.visibility = View.GONE
            }
        }
    }
}
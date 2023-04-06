package com.example.foroshgah_slami.utils

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.foroshgah_slami.R
import java.io.IOException
import java.net.URI

class GlideLoader(val context: Context) {
    fun loadUserPicture(imageURI: Uri, imageView: ImageView) {
        try {
            Glide.with(context)
                .load(imageURI)
                .centerCrop() // Scale type of image
                .placeholder(R.drawable.ic_user_placeholder) // A default place holder if image is failed to load.
                .into(imageView) // the view in witch the image will be loaded
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
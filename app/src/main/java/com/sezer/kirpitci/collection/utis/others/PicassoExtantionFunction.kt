package com.sezer.kirpitci.collection.utis

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.sezer.kirpitci.collection.R
import com.squareup.picasso.Picasso

fun ImageView.updateWithUrl(url: String, imageViewAvatar: ImageView, status: String) {

    if (!url.equals("default")) {
        if(status.equals("true")){
            Glide.with(context).load(url).into(imageViewAvatar)
        }
        else {
             val colorMatrix = ColorMatrix()
            colorMatrix.setSaturation(0f)
            val filter = ColorMatrixColorFilter(colorMatrix)
            imageViewAvatar.setColorFilter(filter)
            Glide.with(context).load(url).into(imageViewAvatar)
        }

    } else {
        Glide.with(context).load(R.drawable.ic_add_new_card_image).into(imageViewAvatar)
    }
}

fun ImageView.updateWithStatusUrl(url: String, imageViewAvatar: ImageView, status: String) {

    if (!url.equals("default")) {
        if (status.equals("false")) {
            Log.d("TAG", "updateWithUrl: --------------" + url)
            Picasso.get()
                .load(url).fit().centerInside()
                .into(imageViewAvatar)
            val matrix = ColorMatrix()
            matrix.setSaturation(0f)
            val filter = ColorMatrixColorFilter(matrix)
            imageViewAvatar.colorFilter = filter
        } else {
            Log.d("TAG", "updateWithUrl: --------------" + url)
            Picasso.get()
                .load(url).fit().centerInside()
                .into(imageViewAvatar)
        }
    } else {
        Glide.with(context).load(R.drawable.ic_add_new_card_image).into(imageViewAvatar)
        Log.d("TAG", "onBindViewHolder: +++++++")
    }
}

fun ImageView.resetImage(imageViewAvatar: ImageView) {

    Glide.with(context).load(R.drawable.ic_add_new_card_image).into(imageViewAvatar)

}

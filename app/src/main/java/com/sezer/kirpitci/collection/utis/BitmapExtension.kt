package com.sezer.kirpitci.collection.utis

import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.sezer.kirpitci.collection.R


fun ImageView.updateWithBitmap(imagePath:String){
    if(!imagePath.equals("") && !imagePath.equals("default") && !imagePath.equals("null")){
        Log.d("TAG", "updateWithBitmap: ")
        val decodedByteArray: ByteArray = Base64.decode(imagePath, Base64.DEFAULT)
        this.setImageBitmap(BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.size))
    }
    else{
        Glide.with(context).load(R.drawable.ic_add_new_card_image).into(this)
    }
}
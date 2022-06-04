package com.sezer.kirpitci.collection.utis.adapters

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sezer.kirpitci.collection.R
import com.sezer.kirpitci.collection.ui.features.registration.CardModel
import com.sezer.kirpitci.collection.utis.others.updateWithUrl
import com.sezer.kirpitci.collection.utis.others.updateWithUrlWithStatus
import com.sezer.kirpitci.collection.utis.others.withURL

class RecyclerAdapter(val listener: ClickItemUser) :
    ListAdapter<CardModel, RecyclerAdapter.WorkerHolder>(
        diffCallback
    ) {
    companion object {
        const val DURING_TIME = 1000
    }
    var lastClick: Long = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkerHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_user_view_card,
            parent,
            false
        )
        return WorkerHolder(itemView)
    }

    override fun onBindViewHolder(holder: WorkerHolder, position: Int) {
        with(getItem(position)) {
            if(cardPath != "default"){
                val colorMatrix = ColorMatrix()
                if(status == "true"){
                    colorMatrix.setSaturation(1f)
                    //holder.cardImage.withURL(this.cardPath, holder.cardImage)
                } else {
                    colorMatrix.setSaturation(0f)
                }
                val filter = ColorMatrixColorFilter(colorMatrix)
                holder.cardImage.colorFilter = filter
                Glide.with(holder.itemView.context).load(this.cardPath).into(holder.cardImage)
            }else if (status.equals("true") && cardPath.equals("default")) {
                Glide.with(holder.itemView.context).load(R.drawable.no_image_drunk).into(holder.cardImage)
            } else if (status.equals("false") && cardPath.equals("default")) {
                Glide.with(holder.itemView.context).load(R.drawable.no_image).into(holder.cardImage)
            } else {

            }

        }
    }

    inner class WorkerHolder(iv: View) : RecyclerView.ViewHolder(iv) {
        val cardImage: ImageView = itemView.findViewById(R.id.imageView)

        init {
            itemView.setOnClickListener {
                if(System.currentTimeMillis()- lastClick > DURING_TIME) {
                    lastClick = System.currentTimeMillis()
                    listener.clicked(getItem(adapterPosition))

                }
            }
        }

    }
}

private val diffCallback = object : DiffUtil.ItemCallback<CardModel>() {
    override fun areItemsTheSame(oldItem: CardModel, newItem: CardModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: CardModel,
        newItem: CardModel
    ): Boolean {
        return oldItem.cardID == newItem.cardID &&
                oldItem.cardName == newItem.cardName &&
                oldItem.cardCity == newItem.cardCity &&
                oldItem.cardCounty == newItem.cardCounty &&
                oldItem.cardInfo == newItem.cardInfo &&
                oldItem.cardPath == newItem.cardPath &&
                oldItem.cardPrice == newItem.cardPrice &&
                oldItem.cardCategory == newItem.cardCategory &&
                oldItem.status == newItem.status &&
                oldItem.userStarRate == newItem.userStarRate
    }
}

package com.sezer.kirpitci.collection.utis.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sezer.kirpitci.collection.R
import com.sezer.kirpitci.collection.ui.features.registration.CardModel
import com.sezer.kirpitci.collection.utis.updateWithUrl

class DetailRecyclerAdapter(val listener: ClickItemUser) : ListAdapter<CardModel, DetailRecyclerAdapter.WorkerHolder>(
    diffCallback
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkerHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_detail,
            parent,
            false
        )
        return WorkerHolder(itemView)
    }

    override fun onBindViewHolder(holder: WorkerHolder, position: Int) {
        with(getItem(position)) {
            holder.cardImage.updateWithUrl(this.cardPath, holder.cardImage, this.status)
        }
    }
    inner class WorkerHolder(iv: View) : RecyclerView.ViewHolder(iv) {
        val cardImage: ImageView = itemView.findViewById(R.id.user_card_view_image)
        init {
            itemView.setOnClickListener{
                listener.clicked(getItem(adapterPosition))
            }
        }
    }
}

private val diffCallback = object : DiffUtil.ItemCallback<CardModel>() {
    override fun areItemsTheSame(oldItem: CardModel, newItem: CardModel): Boolean {
        return oldItem.cardName.equals(newItem.cardName)
    }

    override fun areContentsTheSame(
        oldItem: CardModel,
        newItem: CardModel
    ): Boolean {
        return oldItem.cardID.equals(newItem.cardID)
    }
}

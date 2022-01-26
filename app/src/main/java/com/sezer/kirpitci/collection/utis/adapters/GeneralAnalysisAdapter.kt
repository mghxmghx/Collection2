package com.sezer.kirpitci.collection.utis.adapters

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sezer.kirpitci.collection.R
import com.sezer.kirpitci.collection.ui.features.registration.CardModel
import com.sezer.kirpitci.collection.ui.features.user.ui.generalanalysis.GeneralAnalysisModel
import com.sezer.kirpitci.collection.utis.updateWithUrl
import com.sezer.kirpitci.collection.utis.updateWithUrlWithStatus


class GeneralAnalysisAdapter(val type: Int) : ListAdapter<GeneralAnalysisModel, GeneralAnalysisAdapter.WorkerHolder>(
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
            holder.name.text = this.cardName
            holder.cardImage.updateWithUrlWithStatus(this.cardPath, holder.cardImage, "true")
            holder.rate.isVisible = true
            if(type == 0){
                holder.rate.text =this.average
            } else if(type == 1) {
                holder.rate.text =this.voteCount
            } else if(type == 2) {
                holder.rate.text =this.price
            } else if(type == 3){
                holder.rate.text =this.price
            }

        }
    }
    inner class WorkerHolder(iv: View) : RecyclerView.ViewHolder(iv) {
        val cardImage: ImageView = itemView.findViewById(R.id.user_card_view_image)
        val name: TextView = itemView.findViewById(R.id.detailAlcoholName)
        val rate: TextView = itemView.findViewById(R.id.detailAlcoholRate)
        init {
            itemView.setOnClickListener{
                //listener.clicked(getItem(adapterPosition))
            }
        }

    }
}

private val diffCallback = object : DiffUtil.ItemCallback<GeneralAnalysisModel>() {
    override fun areItemsTheSame(oldItem: GeneralAnalysisModel, newItem: GeneralAnalysisModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: GeneralAnalysisModel,
        newItem: GeneralAnalysisModel
    ): Boolean {
        return oldItem.cardName == newItem.cardName &&
                oldItem.average == newItem.average &&
                oldItem.price == newItem.price &&
                oldItem.voteCount == newItem.voteCount

    }
}
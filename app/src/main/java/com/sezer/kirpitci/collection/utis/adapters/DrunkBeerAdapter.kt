package com.sezer.kirpitci.collection.utis.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sezer.kirpitci.collection.R
import com.sezer.kirpitci.collection.ui.features.registration.CardModel
import com.sezer.kirpitci.collection.ui.features.user.ui.notifications.drunkbeer.DrunkModel
import com.sezer.kirpitci.collection.utis.others.updateWithUrl

class DrunkBeerAdapter() :
    ListAdapter<CardModel, DrunkBeerAdapter.WorkerHolder>(
        diffCallback
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkerHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_drunk_list,
            parent,
            false
        )
        return WorkerHolder(itemView)
    }

    override fun onBindViewHolder(holder: WorkerHolder, position: Int) {
        with(getItem(position)) {
            holder.cardImage.updateWithUrl(this.cardPath, holder.cardImage)
            holder.cardName.text = this.cardName
            val list = ArrayList<ImageView>()
            list.add(holder.starOne)
            list.add(holder.starTwo)
            list.add(holder.starThree)
            list.add(holder.starFour)
            list.add(holder.starFive)
            list.add(holder.starSix)
            list.add(holder.starSeven)
            list.add(holder.starEight)
            list.add(holder.starNine)
            list.add(holder.starTen)
            if(!this.userStarRate.isNullOrEmpty() && this.userStarRate != "null") {
                setStar(this.userStarRate.toString().toInt(), list)

            }
        }
    }
    fun setStar(count: Int, stars: ArrayList<ImageView> ){
        for (i in 0 until count) {
            stars.get(i).setImageResource(R.drawable.ic_dialog_rate_star_check)
        }
    }
    inner class WorkerHolder(iv: View) : RecyclerView.ViewHolder(iv) {
        val cardImage: ImageView = itemView.findViewById(R.id.user_card_view_image)
        val cardName: TextView = itemView.findViewById(R.id.detailAlcoholName)
        val starOne: ImageView = itemView.findViewById(R.id.dialog_star_one)
        val starTwo: ImageView = itemView.findViewById(R.id.dialog_star_two)
        val starThree: ImageView = itemView.findViewById(R.id.dialog_star_three)
        val starFour: ImageView = itemView.findViewById(R.id.dialog_star_four)
        val starFive: ImageView = itemView.findViewById(R.id.dialog_star_five)
        val starSix: ImageView = itemView.findViewById(R.id.dialog_star_six)
        val starSeven: ImageView = itemView.findViewById(R.id.dialog_star_seven)
        val starEight: ImageView = itemView.findViewById(R.id.dialog_star_eight)
        val starNine: ImageView = itemView.findViewById(R.id.dialog_star_nine)
        val starTen: ImageView = itemView.findViewById(R.id.dialog_star_ten)
        init {
            itemView.setOnClickListener {
            }
        }

    }
}

private val diffCallback = object : DiffUtil.ItemCallback<CardModel>() {
    override fun areItemsTheSame(
        oldItem: CardModel,
        newItem: CardModel
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: CardModel,
        newItem: CardModel
    ): Boolean {
        return oldItem == newItem
    }
}
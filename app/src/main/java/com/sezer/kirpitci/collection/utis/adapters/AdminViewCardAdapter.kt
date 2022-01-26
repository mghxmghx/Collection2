package com.sezer.kirpitci.collection.utis.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sezer.kirpitci.collection.R
import com.sezer.kirpitci.collection.ui.features.admin.viewcard.ViewCardModel
import com.sezer.kirpitci.collection.utis.updateWithUrlWithStatus

class AdminViewCardAdapter(private val listener: ClickListener) :
    ListAdapter<ViewCardModel, AdminViewCardAdapter.UserHolder>(
        diffCallback
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_admin_view_card, parent,
            false
        )
        return UserHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        //val currentCard = getItem(position)
        with(getItem(position)) {
            holder.image.updateWithUrlWithStatus(this.cardPath, holder.image, true.toString())
            holder.cardName.text = this.cardName
            holder.cardCountry.text = this.cardCounty
            Log.d("TAG", "onBindViewHolder: " + this.cardID)
        }

        //holder.image.updateWithUrl(currentCard.cardPath,holder.image)

    }

    inner class UserHolder(iv: View) : RecyclerView.ViewHolder(iv), View.OnClickListener {
        val image: ImageView = iv.findViewById(R.id.card_image)
        val cardName: TextView = iv.findViewById(R.id.card_name)
        val cardCountry: TextView = iv.findViewById(R.id.card_country)
        val deleteButton: ImageView = iv.findViewById(R.id.delete)

        init {
            itemView.findViewById<ImageView>(R.id.delete).setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            if (p0 == deleteButton) {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    getItem(adapterPosition)?.let {
                        listener.itemDeleteClick(it)
                    }
                }
            }
        }
    }

}

private val diffCallback = object : DiffUtil.ItemCallback<ViewCardModel>() {
    override fun areItemsTheSame(oldItem: ViewCardModel, newItem: ViewCardModel): Boolean {
        return oldItem.cardID.equals(newItem.cardID)
    }

    override fun areContentsTheSame(
        oldItem: ViewCardModel,
        newItem: ViewCardModel
    ): Boolean {
        return oldItem.cardPath.equals(newItem.cardPath)
    }
}
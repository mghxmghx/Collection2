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
import com.sezer.kirpitci.collection.utis.updateWithUrlWithStatus
import org.w3c.dom.Text
import java.util.*
import kotlin.collections.ArrayList

class DetailRecyclerAdapter(val listener: ClickItemUser) :
    ListAdapter<CardModel, DetailRecyclerAdapter.WorkerHolder>(
        diffCallback
    ) {
    // private lateinit var list: List<Drawable>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkerHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_list,
            parent,
            false
        )
        setContent()
        return WorkerHolder(itemView)
    }

    private lateinit var listPath: ArrayList<Int>
    private lateinit var listName: ArrayList<String>
    private fun setContent() {
        listPath = arrayListOf()
        listName = arrayListOf()
        listPath.add(R.drawable.germany)
        listPath.add(R.drawable.russia)
        listPath.add(R.drawable.ukraine)
        listName.add("germany")
        listName.add("russia")
        listName.add("ukraine")
    }

    override fun onBindViewHolder(holder: WorkerHolder, position: Int) {
        with(getItem(position)) {
            holder.cardImage.updateWithUrlWithStatus(
                this.cardPath,
                holder.cardImage,
                true.toString()
            )
            holder.nameText.text = this.cardName
            holder.detailAlcoholRate.text = this.cardABV
            holder.alcoholType.text = this.cardCompany

            val index = listName.indexOf(this.cardCounty.lowercase(Locale.getDefault()))
            if (index != -1) {
                holder.cardFlag.setImageResource(listPath.get(index))
            }
        }
    }

    inner class WorkerHolder(iv: View) : RecyclerView.ViewHolder(iv) {
        val cardImage: ImageView = itemView.findViewById(R.id.user_card_view_image)
        val nameText: TextView = itemView.findViewById(R.id.detailAlcoholName)
        val detailAlcoholRate: TextView = itemView.findViewById(R.id.detailAlcoholRate)
        val alcoholType: TextView = itemView.findViewById(R.id.alcoholType)
        val alcoholML: TextView = itemView.findViewById(R.id.alcoholML)
        val cardFlag: ImageView = itemView.findViewById(R.id.user_card_view_flag)
        init {
            iv.setOnClickListener {
                listener.clicked(getItem(adapterPosition))
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
                oldItem.status == newItem.status
    }
}

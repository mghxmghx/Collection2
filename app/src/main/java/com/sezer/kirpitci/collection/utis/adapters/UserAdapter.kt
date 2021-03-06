package com.sezer.kirpitci.collection.utis.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sezer.kirpitci.collection.R
import com.sezer.kirpitci.collection.ui.features.registration.CardModel

class UserAdapter(initCList: List<CardModel>, val listener: ClickItemUser) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private val modelList = mutableListOf<CardModel>()

    init {
        modelList.addAll(initCList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_user_view_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = modelList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(model = modelList[position])
        holder.itemView.findViewById<ImageView>(R.id.user_card_view_image).setOnClickListener {
            listener.clicked(modelList[position])
        }
    }

    fun swap(modelList: List<CardModel>) {
        val diffCallback = DiffUtilUserRecycler(this.modelList, modelList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.modelList.clear()
        this.modelList.addAll(modelList)
        diffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardImage: ImageView = itemView.findViewById(R.id.user_card_view_image)
        fun bind(model: CardModel) {
        }
    }
}
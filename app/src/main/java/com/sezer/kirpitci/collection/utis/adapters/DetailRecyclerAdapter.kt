package com.sezer.kirpitci.collection.utis.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sezer.kirpitci.collection.R
import com.sezer.kirpitci.collection.ui.features.admin.viewcard.ViewCardStatusModel
import com.sezer.kirpitci.collection.utis.updateWithBitmap


class DetailRecyclerAdapter(initCList: List<ViewCardStatusModel>, val listener: ClickItemUser) :
    RecyclerView.Adapter<DetailRecyclerAdapter.ViewHolder>() {

    private val modelList = mutableListOf<ViewCardStatusModel>()

    init {
        modelList.addAll(initCList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_detail, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = modelList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(model = modelList[position])
        holder.itemView.findViewById<ImageView>(R.id.user_card_view_image).setOnClickListener {
            listener.clicked(modelList[position])
        }
    }

    fun swap(modelList: List<ViewCardStatusModel>) {
        val diffCallback = DiffUtilUserRecycler(this.modelList, modelList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.modelList.clear()
        this.modelList.addAll(modelList)
        diffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardImage: ImageView = itemView.findViewById(R.id.user_card_view_image)
        private val cardDetailName: TextView = itemView.findViewById(R.id.detailAlcoholName)
        fun bind(model: ViewCardStatusModel) {
            cardImage.updateWithBitmap(model.cardPath)
            cardDetailName.text = model.cardName
        }
    }
}
package com.sezer.kirpitci.collection.utis.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sezer.kirpitci.collection.R
import com.sezer.kirpitci.collection.ui.features.admin.viewcard.ViewCardModel
import com.sezer.kirpitci.collection.utis.DiffUtilRecycler
import com.sezer.kirpitci.collection.utis.updateWithBitmap
import com.sezer.kirpitci.collection.utis.updateWithUrl

class AdapterX(initCList: List<ViewCardModel>, val listener: ClickListener) :
    RecyclerView.Adapter<AdapterX.ViewHolder>() {

    private val modelList = mutableListOf<ViewCardModel>()

    init {
        modelList.addAll(initCList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_admin_view_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = modelList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(model = modelList[position])
        val deleteButton = holder.itemView.findViewById<ImageView>(R.id.delete)
        val updateButton = holder.itemView.findViewById<ImageView>(R.id.update)
        deleteButton.setOnClickListener {

            if (modelList.size == 1) {
                listener.itemDeleteClick(modelList[0])
            } else {
                listener.itemDeleteClick(modelList[position])
            }
        }
        updateButton.setOnClickListener {

            if (modelList.size == 1) {
                listener.itemUpdateClick(modelList[0])
            } else {
                listener.itemUpdateClick(modelList[position])
            }
        }
    }

    fun swap(modelList: List<ViewCardModel>) {
        val diffCallback = DiffUtilRecycler(this.modelList, modelList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.modelList.clear()
        this.modelList.addAll(modelList)
        diffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val code: TextView = itemView.findViewById(R.id.card_name)
        private val name: TextView = itemView.findViewById(R.id.card_country)
        private val delete: ImageView = itemView.findViewById(R.id.delete)
        private val update: ImageView = itemView.findViewById(R.id.update)
        private val cardImage: ImageView = itemView.findViewById(R.id.card_image)

        fun bind(model: ViewCardModel) {
            cardImage.updateWithUrl(model.cardPath, cardImage)
            code.text = model.cardName
            name.text = model.cardCounty
        }
    }
}
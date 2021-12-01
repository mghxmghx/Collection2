package com.sezer.kirpitci.collection.utis.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sezer.kirpitci.collection.R
import com.sezer.kirpitci.collection.ui.features.registration.calisma.TrainingModelReal


class AdapterUserView(initCList: List<TrainingModelReal>) :
    RecyclerView.Adapter<AdapterUserView.ViewHolder>() {

    private val modelList = mutableListOf<TrainingModelReal>()

    init {
        modelList.addAll(initCList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_rcy_viewuser, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = modelList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(model = modelList[position])

    }

    fun swap(modelList: List<TrainingModelReal>) {
        val diffCallback = DiffUtilUserViewRecycler(this.modelList, modelList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.modelList.clear()
        this.modelList.addAll(modelList)
        diffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val name: TextView = itemView.findViewById(R.id.rcy_name)
        private val email: TextView = itemView.findViewById(R.id.rcy_email)
        private val status: TextView = itemView.findViewById(R.id.rcy_status)

        fun bind(model: TrainingModelReal) {
            name.text = model.userName
            email.text = model.email
            status.text = model.status
        }
    }
}
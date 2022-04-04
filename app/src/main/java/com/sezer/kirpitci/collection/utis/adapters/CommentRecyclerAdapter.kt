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
import com.sezer.kirpitci.collection.ui.features.user.ui.beer.CommentModel
import java.util.*


class CommentRecyclerAdapter() :
    ListAdapter<CommentModel, CommentRecyclerAdapter.WorkerHolder>(
        diffCallback
    ) {
    // private lateinit var list: List<Drawable>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkerHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_comment,
            parent,
            false
        )
        return WorkerHolder(itemView)
    }
    override fun onBindViewHolder(holder: WorkerHolder, position: Int) {
        with(getItem(position)) {
            Log.d("TAG", "onBindViewHolder: " + this.comment)
            holder.comment.text = this.comment
        }
    }

    inner class WorkerHolder(iv: View) : RecyclerView.ViewHolder(iv) {
        val comment: TextView = iv.findViewById(R.id.comment)
    }
}

private val diffCallback = object : DiffUtil.ItemCallback<CommentModel>() {
    override fun areItemsTheSame(oldItem: CommentModel, newItem: CommentModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: CommentModel,
        newItem: CommentModel
    ): Boolean {
        return oldItem.comment == newItem.comment
    }
}

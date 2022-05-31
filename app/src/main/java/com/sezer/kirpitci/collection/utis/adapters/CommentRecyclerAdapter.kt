package com.sezer.kirpitci.collection.utis.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sezer.kirpitci.collection.R
import com.sezer.kirpitci.collection.ui.features.user.ui.beer.CommentModel
import java.util.*


class CommentRecyclerAdapter :
    ListAdapter<CommentModel, CommentRecyclerAdapter.WorkerHolder>(
        diffCallback
    ) {
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
            holder.comment.text = this.comment
            holder.commentUser.text = this.commentUser
            holder.commentTime.text = getShortDate(this.commentTime.toLong())
        }
    }

    private fun getShortDate(ts: Long?): String {
        if (ts == null) return ""
        val calendar = Calendar.getInstance(Locale.getDefault())
        calendar.timeInMillis = ts
        return android.text.format.DateFormat.format("dd/MM/yyyy", calendar).toString()
    }

    inner class WorkerHolder(iv: View) : RecyclerView.ViewHolder(iv) {
        val comment: TextView = iv.findViewById(R.id.comment)
        val commentUser: TextView = iv.findViewById(R.id.commentUser)
        val commentTime: TextView = iv.findViewById(R.id.commentTime)
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
        return oldItem.comment == newItem.comment &&
                oldItem.commentTime == newItem.commentTime &&
                oldItem.commentUser == newItem.commentUser
    }
}

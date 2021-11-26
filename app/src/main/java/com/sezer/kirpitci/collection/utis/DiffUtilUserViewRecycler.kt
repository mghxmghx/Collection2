package com.sezer.kirpitci.collection.utis

import androidx.recyclerview.widget.DiffUtil
import com.sezer.kirpitci.collection.ui.features.admin.viewcard.ViewCardModel
import com.sezer.kirpitci.collection.ui.features.admin.viewcard.ViewCardStatusModel
import com.sezer.kirpitci.collection.ui.features.registration.calisma.TrainingModelReal


class DiffUtilUserViewRecycler(
    private val oldList: List<TrainingModelReal>,
    private val newList: List<TrainingModelReal>
) : DiffUtil.Callback(){

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].userName.equals(newList[newItemPosition].userName)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].email.equals(newList[newItemPosition].email)
    }

}
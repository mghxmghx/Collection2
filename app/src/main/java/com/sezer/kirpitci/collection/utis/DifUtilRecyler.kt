package com.sezer.kirpitci.collection.utis

import androidx.recyclerview.widget.DiffUtil
import com.sezer.kirpitci.collection.ui.features.admin.viewcard.AdminViewCardViewModel
import com.sezer.kirpitci.collection.ui.features.admin.viewcard.ViewCardModel

class DiffUtilRecycler(
    private val oldList: List<ViewCardModel>,
    private val newList: List<ViewCardModel>
) : DiffUtil.Callback(){

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].cardName.equals(newList[newItemPosition].cardName)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].cardID.equals(newList[newItemPosition].cardID)
    }

}


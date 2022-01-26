package com.sezer.kirpitci.collection.utis.adapters

import androidx.recyclerview.widget.DiffUtil
import com.sezer.kirpitci.collection.ui.features.registration.CardModel


class DiffUtilUserRecycler(
    private val oldList: List<CardModel>,
    private val newList: List<CardModel>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].cardName.equals(newList[newItemPosition].cardName)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].cardID.equals(newList[newItemPosition].cardID)
    }

}